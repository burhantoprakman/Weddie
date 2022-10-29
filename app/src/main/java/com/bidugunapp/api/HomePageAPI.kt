package com.bidugunapp.api

import com.bidugunapp.model.EventInfo
import retrofit2.Response
import retrofit2.http.GET

interface HomePageAPI {
    @GET("/homepage")
    suspend fun getHomePageData(
    ) : Response<EventInfo>
}