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
import com.dcard.gakki.api.PostListService
import com.dcard.gakki.api.PostModel
import com.dcard.gakki.list.GaggiListAdapter
import com.google.android.gms.common.util.ListUtils
import com.google.maps.android.clustering.Cluster
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_bottom_sheet_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 */
class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        ClusterManager.OnClusterItemClickListener<GakkiClusterItem>,
        ClusterManager.OnClusterClickListener<GakkiClusterItem>,
        GakkiRenderer.GakkiRendererListener {

    private val DEFAULT_ZOOM = 15f

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    private var mClusterManager: ClusterManager<GakkiClusterItem>? = null
    private var mSheetBehavior: BottomSheetBehavior<*>? = null
    private var mPostList: List<PostModel>? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private lateinit var service: PostListService
    private val recordPostIdSet = HashSet<String>()

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

    private fun setupRecyclerView() {
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

        setupApiService()

        updateLocationUI()

        setupMapListener()

        setupCluster()

        getDeviceLocation()

//        loadApi()

//        mMap.addMarker(
//                MarkerOptions().position(sydney).title("Marker in Sydney")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onClusterItemClick(item: GakkiClusterItem): Boolean {
        Log.d("badu", "onClusterItemClick")


        return true
    }

    override fun onClusterClick(cluster: Cluster<GakkiClusterItem>): Boolean {
        Log.d("badu", "onClusterClick")

        return true
    }

    override fun onGakkiCameraIdel() {

        val latlon = mMap.cameraPosition.target
        val zoom = mMap.cameraPosition.zoom
        val latlonString = getLatlonString(latlon.latitude,latlon.longitude)

        Log.d("badu", "gakki idle  #####  $latlonString")
        loadApi(latlonString)
    }

    private fun loadApi(
            location: String = "25.0478,121.5318",
            radius: Int = 1200
    ) {
        service.getCollectionPost(location, radius)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it ->
                    val newList = mutableListOf<PostModel>()
                    it.forEach{
                        if(!recordPostIdSet.contains(it.id)){
                            newList.add(it)
                            recordPostIdSet.add(it.id)
                        }
                    }
                    newList
                }
                .subscribe({

                    val newList = mutableListOf<PostModel>()
                    if(mPostList != null && !mPostList!!.isEmpty()) {
                        newList.addAll(mPostList!!)
                    }
                    newList.addAll(it)

                    mPostList = newList
                    addItemToCluster(it)

                    Log.d("badu", "success size : " + it.size + " | " + mPostList!!.size)

                }, {
                    Log.w("badu", "fail")
                    Log.e("badu",it.toString())
                })
    }

    private fun setupApiService(){
        val reprofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://35.194.137.25:3100/hackathon/")
                .build()

        service = reprofit.create(PostListService::class.java)
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
            setOnMarkerClickListener(mClusterManager)
            setOnCameraIdleListener(mClusterManager)

            mClusterManager?.run {
                renderer = GakkiRenderer(this@MapsActivity, this, mMap).apply {
                    listener = this@MapsActivity
                }
                setOnClusterClickListener(this@MapsActivity)
                setOnClusterItemClickListener(this@MapsActivity)
            }
        }

//        addItemToCluster()
    }

    private fun addItemToCluster(postList: List<PostModel>) {
        mPostList?.run {
            mClusterManager?.run {

                postList.forEach {
                    addItem(GakkiClusterItem(LatLng(it.latitude.toDouble(), it.longitude.toDouble())))
                }

            }
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
                mLatitude = mLastKnownLocation!!.latitude
                mLongitude = mLastKnownLocation!!.longitude
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude), DEFAULT_ZOOM))

//                loadApi()
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

    private fun getLatlonString(lat: Double, lon: Double) : String{
        val latString = lat.toString().run {
            if(this.length < 8) {
                this
            }else {
                this.substring(0,7)
            }
        }

        val lonString = lon.toString().run {
            if(this.length < 8) {
                this
            }else {
                this.substring(0,7)
            }
        }

        return "$latString,$lonString"

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
