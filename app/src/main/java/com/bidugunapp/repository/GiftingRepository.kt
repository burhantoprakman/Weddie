package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance

class GiftingRepository {
    suspend fun getGifting() = RetrofitInstance.giftingAPI.getGifting()
}