package com.bidugunapp.model

import android.graphics.Bitmap

data class GuestBookResponse(
    val description: String,
    val eventId: Int,
    val guestBookId: Int,
    val photoUrl: String,
    val photoBitmap: Bitmap,
    val title: String,
    val visible: Boolean
)