package com.bidugunapp.api

import com.bidugunapp.model.GuestBook
import retrofit2.Response
import retrofit2.http.GET

interface GuestBookAPI {
    @GET("/guestBook")
    suspend fun getGuestBookList(

    ): Response<GuestBook>
}