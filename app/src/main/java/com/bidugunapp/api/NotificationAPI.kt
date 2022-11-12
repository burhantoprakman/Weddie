package com.bidugunapp.api

import com.bidugunapp.model.PushNotification
import com.bidugunapp.util.Constants.Companion.CONTENT_TYPE
import com.bidugunapp.util.Constants.Companion.FIREBASE_BASE_URL
import com.bidugunapp.util.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("$FIREBASE_BASE_URL/fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}