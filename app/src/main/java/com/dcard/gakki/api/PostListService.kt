package com.dcard.gakki.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PostListService {
    @GET("posts")
    fun getCollectionPost(
            @Query("location") location: String,
            @Query("radius") radius: Int
    ): Single<List<PostListModel>>
}


//  http://35.194.137.25:3100/hackathon/posts?location=25.041651,121.544041&radius=1000