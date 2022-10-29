package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance
import retrofit2.Retrofit

class GuestBookRepository   {
    suspend fun getGuestBookList() = RetrofitInstance.guestBookApi.getGuestBookList()

}