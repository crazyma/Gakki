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


        val bitmap = BitmapFactory.decodeResource(context.resources,
                R.drawable.level4)

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.level4
        )).title(item.title)


    }

    override fun onBeforeClusterRendered(cluster: Cluster<GakkiClusterItem>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)

        val resId = when{
            cluster.size >= 200 -> {
                R.drawable.level1
            }

            cluster.size >= 50 -> {
                R.drawable.level2
            }

            cluster.size >= 10 -> {
                R.drawable.level3
            }

            else -> {
                R.drawable.level4
            }
        }

        markerOptions.icon(BitmapDescriptorFactory.fromResource(resId
        )).title(cluster.items.first().title)

    }

    override fun onCameraIdle() {
        listener?.onGakkiCameraIdel()
    }
}