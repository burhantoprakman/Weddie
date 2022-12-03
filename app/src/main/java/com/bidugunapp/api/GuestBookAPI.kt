package com.bidugunapp.api

import android.graphics.Bitmap
import com.bidugunapp.model.GuestBook
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GuestBookAPI {
    @GET("/guestBook")
    suspend fun getGuestBookList(
        ): Response<GuestBook>

    @GET("/addWishes")
    suspend fun addWishes(
        @Query("photoBitmap")
        photoBitmap : Bitmap,
        @Query("title")
        title : String,
        @Query("description")
        description : String,
        @Query("visibility")
        visible : Boolean,
    ) : Response<GuestBook>
}