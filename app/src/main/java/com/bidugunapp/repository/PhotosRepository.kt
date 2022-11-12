package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance

class PhotosRepository {

    suspend fun getPhotos() = RetrofitInstance.photosApi.getPhotos()
}