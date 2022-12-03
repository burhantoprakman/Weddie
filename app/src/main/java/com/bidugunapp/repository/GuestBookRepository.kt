package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance
import com.bidugunapp.model.GuestBookResponse

class GuestBookRepository {
    suspend fun getGuestBookList() = RetrofitInstance.guestBookApi.getGuestBookList()
    suspend fun addWishes(guestBookResponse: GuestBookResponse) =
        RetrofitInstance.guestBookApi.addWishes(
            guestBookResponse.photoBitmap,
            guestBookResponse.title,
            guestBookResponse.description,
            guestBookResponse.visible
        )
}