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

    private val mIconGenerator1 = IconGenerator(context)
    private val mIconGenerator2 = IconGenerator(context)
    private val mIconGenerator3 = IconGenerator(context)
    private val mIconGenerator4 = IconGenerator(context)
    private var mTextView1: TextView? = null
    private var mTextView2: TextView? = null
    private var mTextView3: TextView? = null
    private var mTextView4: TextView? = null
    var listener: GakkiRendererListener? = null

    init {
        val TRANSPARENT_DRAWABLE = ColorDrawable(Color.TRANSPARENT)

        val floor1Layout = LayoutInflater.from(context).inflate(R.layout.layout_floor_1, null)
        val floor2Layout = LayoutInflater.from(context).inflate(R.layout.layout_floor_2, null)
        val floor3Layout = LayoutInflater.from(context).inflate(R.layout.layout_floor_3, null)
        val floor4Layout = LayoutInflater.from(context).inflate(R.layout.layout_floor_4, null)


        mTextView1 = floor1Layout.findViewById(R.id.floorTextView1)
        mTextView2 = floor2Layout.findViewById(R.id.floorTextView2)
        mTextView3 = floor3Layout.findViewById(R.id.floorTextView3)
        mTextView4 = floor4Layout.findViewById(R.id.floorTextView4)

        mIconGenerator1.setContentView(floor1Layout)
        mIconGenerator1.setBackground(TRANSPARENT_DRAWABLE)

        mIconGenerator2.setContentView(floor2Layout)
        mIconGenerator2.setBackground(TRANSPARENT_DRAWABLE)

        mIconGenerator3.setContentView(floor3Layout)
        mIconGenerator3.setBackground(TRANSPARENT_DRAWABLE)

        mIconGenerator4.setContentView(floor4Layout)
        mIconGenerator4.setBackground(TRANSPARENT_DRAWABLE)

    }

    override fun onBeforeClusterItemRendered(item: GakkiClusterItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)

//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.level4
//        )).title(item.title)

        mTextView1?.text = "1"
        val icon = mIconGenerator1.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.title)

    }

    override fun onBeforeClusterRendered(cluster: Cluster<GakkiClusterItem>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)

        val icon = when{
            cluster.size >= 200 -> {
                mTextView4?.text = cluster.size.toString()
                mIconGenerator4.makeIcon()
            }

            cluster.size >= 50 -> {
                mTextView3?.text = cluster.size.toString()
                mIconGenerator3.makeIcon()
            }

            cluster.size >= 10 -> {
                mTextView2?.text = cluster.size.toString()
                mIconGenerator2.makeIcon()
            }

            else -> {
                mTextView1?.text = cluster.size.toString()
                mIconGenerator1.makeIcon()
            }
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(cluster.items.first().title)

    }

    override fun onCameraIdle() {
        listener?.onGakkiCameraIdel()
    }
}