package com.example.retrotrade.model

import com.example.retrotrade.rest.model.response.LoadItemsResponse
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MapClusterItem(
    val item: LoadItemsResponse
) : ClusterItem {
    override fun getPosition() = LatLng(item.latitude, item.longitude)
    override fun getTitle()    = item.name
    override fun getSnippet()  = item.category.label
    override fun getZIndex()   = 0f
}

fun LoadItemsResponse.toClusterItem() = MapClusterItem(this)