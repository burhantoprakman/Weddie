package com.bidugunapp.model

data class EventInfo(
    val date: String,
    val dressCode: String,
    val eventId: Int,
    val foodMenu: FoodMenu,
    val gmapsLocationLat: String,
    val gmapsLocationLng: String,
    val guestBookOnay: Boolean,
    val guestListOnay: Boolean,
    val location: String,
    val name: String,
    val openChatOnay: Boolean,
    val photoOnay: Boolean,
    val photos: List<String>,
    val text: String,
    val waitingRoomOnay: Boolean
)