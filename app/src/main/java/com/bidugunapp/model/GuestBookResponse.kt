package com.bidugunapp.model

data class GuestBookResponse(
    val description: String,
    val eventId: Int,
    val guestBookId: Int,
    val photoUrl: String,
    val title: String,
    val visible: Boolean
)