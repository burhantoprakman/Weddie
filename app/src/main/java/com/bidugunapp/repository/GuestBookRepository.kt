package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance

class GuestBookRepository {
    suspend fun getGuestBookList() = RetrofitInstance.guestBookApi.getGuestBookList()

}