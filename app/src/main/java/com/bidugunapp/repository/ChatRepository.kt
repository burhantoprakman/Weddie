package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance
import com.bidugunapp.model.PushNotification

class ChatRepository() {
    suspend fun sendNotification(pushNotification: PushNotification) =
        RetrofitInstance.notificationAPI.postNotification(pushNotification)
}