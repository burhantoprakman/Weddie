package com.bidugunapp.model

data class Photo(
    val id: Int,
    val url: String,
    val likedCount : Int,
    var liked : Boolean = false
)