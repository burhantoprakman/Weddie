package com.bidugunapp.api

import com.bidugunapp.model.Photo
import com.bidugunapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val guestBookApi: GuestBookAPI by lazy {
            retrofit.create(GuestBookAPI::class.java)
        }
        val homePageApi: HomePageAPI by lazy {
            retrofit.create(HomePageAPI::class.java)
        }
        val photosApi: PhotosAPI by lazy {
            retrofit.create(PhotosAPI::class.java)
        }

        val notificationAPI: NotificationAPI by lazy {
            retrofit.create(NotificationAPI::class.java)
        }

        val giftingAPI: GiftingAPI by lazy {
            retrofit.create(GiftingAPI::class.java)
        }
    }
}