package com.dcard.gakki

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.layout_test.*
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_bottom_sheet_list.*


/**
 * https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val DEFAULT_ZOOM = 15f

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    private var mClusterManager: ClusterManager<GakkiClusterItem>? = null
    private var mSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMap()

        setupBottomSheet()

        button.setOnClickListener {
            Log.d("badu", "XD")
            mSheetBehavior?.run {
                when (state) {
                    STATE_HIDDEN -> {
                        this.state = STATE_COLLAPSED
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private fun setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupBottomSheet() {
        mSheetBehavior = BottomSheetBehavior.from(bottomSheetBase)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        recyclerView.apply {

            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(this@MapsActivity)
            adapter = GaggiListAdapter(this@MapsActivity)

        }
    }

    override fun onBackPressed() {

        mSheetBehavior?.run {
            when (state) {
                STATE_EXPANDED -> {
                    state = STATE_COLLAPSED
                }

                STATE_COLLAPSED -> {
                    state = STATE_HIDDEN
                }

                else -> {
                    super.onBackPressed()
                }
            }
        } ?: run {
            super.onBackPressed()
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        updateLocationUI()

        getDeviceLocation()

        setupMapListener()

        setupCluster()

//        mMap.addMarker(
//                MarkerOptions().position(sydney).title("Marker in Sydney")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun setupMapListener() {

        mMap.setOnCameraMoveListener {
            Log.d("badu", "camera move")
        }

        mMap.setOnCameraMoveStartedListener {
            Log.d("badu", "camera move start")
        }

        mMap.setOnCameraIdleListener {

            val latlon = mMap.cameraPosition.target
            val zoom = mMap.cameraPosition.zoom
            Log.d("badu", "camera idle | latlen $latlon , zoom : $zoom")
        }
    }

    private fun setupCluster() {

        mMap.run {
            mClusterManager = ClusterManager(this@MapsActivity, this)
            setOnCameraIdleListener(mClusterManager)
        }

        addItemToCluster()
    }

    private fun addItemToCluster() {

        mClusterManager?.run {

            addItem(GakkiClusterItem(LatLng(25.073184697300164, 121.51969324797392)))
            addItem(GakkiClusterItem(LatLng(25.072380544381666, 121.51840344071388)))
            addItem(GakkiClusterItem(LatLng(25.06792632834905, 121.51950985193253)))
            addItem(GakkiClusterItem(LatLng(25.06693810028716, 121.52444478124382)))
            addItem(GakkiClusterItem(LatLng(25.070801680447058, 121.52446053922176)))
            addItem(GakkiClusterItem(LatLng(25.077315615627185, 121.52225375175475)))
            addItem(GakkiClusterItem(LatLng(25.076663630355, 121.51596732437609)))
            addItem(GakkiClusterItem(LatLng(25.070844196765062, 121.51124596595764)))
            addItem(GakkiClusterItem(LatLng(25.066656268573674, 121.51652187108995)))

        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

//        try {
//            if (mLocationPermissionGranted) {
//                mMap.isMyLocationEnabled = true
//                mMap.uiSettings.isMyLocationButtonEnabled = true
//            } else {
//                mMap.isMyLocationEnabled = false
//                mMap.uiSettings.isMyLocationButtonEnabled = false
//                mLastKnownLocation = null
//                getLocationPermission()
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {

        val locationResult = mFusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener {
            if (it.isSuccessful) {
                mLastKnownLocation = it.result as Location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude), DEFAULT_ZOOM))
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0), DEFAULT_ZOOM))
                mMap.uiSettings.isMyLocationButtonEnabled = false
            }
        }

//        locationResult.addOnCompleteListener(this, object : OnCompleteListener {
//            override fun onComplete(task: Task<*>) {
//                if (task.isSuccessful) {
//                    // Set the map's camera position to the current location of the device.
//                    val mLastKnownLocation = task.result as Location
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                            LatLng(mLastKnownLocation.latitude,
//                                    mLastKnownLocation.longitude), DEFAULT_ZOOM))
//                } else {
//                    Log.d(FragmentActivity.TAG, "Current location is null. Using defaults.")
//                    Log.e(FragmentActivity.TAG, "Exception: %s", task.exception)
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
//                    mMap.uiSettings.isMyLocationButtonEnabled = false
//                }
//            }
//        })

//        try {
//            if (mLocationPermissionGranted) {
//                val locationResult = mFusedLocationProviderClient.getLastLocation()
//                locationResult.addOnCompleteListener(this, object : OnCompleteListener {
//                    override fun onComplete(task: Task<*>) {
//                        if (task.isSuccessful) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.result
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM))
//                        } else {
//                            Log.d(FragmentActivity.TAG, "Current location is null. Using defaults.")
//                            Log.e(FragmentActivity.TAG, "Exception: %s", task.exception)
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
//                            mMap.uiSettings.isMyLocationButtonEnabled = false
//                        }
//                    }
//                })
//            }
//        } catch (e: SecurityException) {
//            Log.e("Exception: %s", e.message)
//        }

    }
}

/* scale to meter
20 : 1128.497220
19 : 2256.994440
18 : 4513.988880
17 : 9027.977761
16 : 18055.955520
15 : 36111.911040
14 : 72223.822090
13 : 144447.644200
12 : 288895.288400
11 : 577790.576700
10 : 1155581.153000
9  : 2311162.307000
8  : 4622324.614000
7  : 9244649.227000
6  : 18489298.450000
5  : 36978596.910000
4  : 73957193.820000
3  : 147914387.600000
2  : 295828775.300000
1  : 591657550.500000
 */
