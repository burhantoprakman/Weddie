package com.bidugunapp.api

import com.bidugunapp.model.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET

interface PhotosAPI {

    @GET("/photos")
    suspend fun getPhotos(
    ): Response<PhotosResponse>
}