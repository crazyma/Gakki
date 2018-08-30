package com.dcard.gakki

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val DEFAULT_ZOOM = 15f

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation : Location?  =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)

        updateLocationUI()

        getDeviceLocation()

//        mMap.addMarker(
//                MarkerOptions().position(sydney).title("Marker in Sydney")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
        /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */


        val locationResult = mFusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener {
            if(it.isSuccessful){
                mLastKnownLocation = it.result as Location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude),DEFAULT_ZOOM))
            }else{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-34.0, 151.0),DEFAULT_ZOOM))
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
