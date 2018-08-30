package com.dcard.gakki

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class GakkiClusterItem(var latLng: LatLng) : ClusterItem {


    override fun getSnippet(): String {
        return latLng.toString()
    }

    override fun getTitle(): String {
        return latLng.toString()
    }

    override fun getPosition(): LatLng {
        return latLng
    }
}