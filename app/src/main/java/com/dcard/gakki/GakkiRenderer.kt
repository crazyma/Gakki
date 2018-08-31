package com.dcard.gakki

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class GakkiRenderer(
        val context: Context,
        clusterManager: ClusterManager<GakkiClusterItem>,
        map: GoogleMap
): DefaultClusterRenderer<GakkiClusterItem>(context,map,clusterManager), GoogleMap.OnCameraIdleListener {

    interface GakkiRendererListener{
        fun onGakkiCameraIdel()
    }

    private val mIconGenerator = IconGenerator(context)
    private var mTextView: TextView? = null
    var listener: GakkiRendererListener? = null

    init {
        val multiProfile = LayoutInflater.from(context).inflate(R.layout.layout_floor_2, null)
        val TRANSPARENT_DRAWABLE = ColorDrawable(Color.TRANSPARENT)

// Make the background of marker transparent
//        mIconGenerator.setBackground(TRANSPARENT_DRAWABLE);
        mTextView = multiProfile.findViewById(R.id.floorTextView)
        mIconGenerator.setContentView(multiProfile)
        mIconGenerator.setBackground(TRANSPARENT_DRAWABLE)
    }

    override fun onBeforeClusterItemRendered(item: GakkiClusterItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)

//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.level4
//        )).title(item.title)

        mTextView?.text = "8"
        val icon = mIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)

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