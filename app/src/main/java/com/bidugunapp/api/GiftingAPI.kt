package com.bidugunapp.api

import com.bidugunapp.model.Gift
import retrofit2.Response
import retrofit2.http.GET

interface GiftingAPI {

    @GET("/gifting")
    suspend fun getGifting(
    ) : Response<Gift>
}