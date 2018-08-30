package com.dcard.gakki

import android.content.Context
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class GakkiRenderer(
        val context: Context,
        clusterManager: ClusterManager<GakkiClusterItem>,
        map: GoogleMap
): DefaultClusterRenderer<GakkiClusterItem>(context,map,clusterManager), GoogleMap.OnCameraIdleListener {

    interface GakkiRendererListener{
        fun onGakkiCameraIdel()
    }

    var listener: GakkiRendererListener? = null

    override fun onBeforeClusterItemRendered(item: GakkiClusterItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        /*
        val bitmap = BitmapFactory.decodeResource(context.resources,
                R.drawable.small_icon)

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.small_icon
        )).title(item.title)
        */


    }

    override fun onCameraIdle() {
        listener?.onGakkiCameraIdel()
    }
}