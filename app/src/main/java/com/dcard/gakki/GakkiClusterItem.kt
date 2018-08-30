package com.dcard.gakki

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class GakkiClusterItem(
        var latLng: LatLng,
        var postId: String,
        var postTitle: String
) : ClusterItem {


    override fun getSnippet(): String {
        return postId
    }

    override fun getTitle(): String {
        return postTitle
    }

    override fun getPosition(): LatLng {
        return latLng
    }
}