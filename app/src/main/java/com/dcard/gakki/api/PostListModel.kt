package com.dcard.gakki.api

data class PostListModel(
        var id: String,
        var postId: String,
        var title: String,
        var content: String,
        var forum: String,
        var ip: String,
        var memberId: String,
        var gender: String,
        var latitude: Float,
        var longitude: Float,
        var publishedAt: String,
        var createdAt: String,
        var updatedAt: String,
        var thumbnail: String
)