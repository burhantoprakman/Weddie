package com.bidugunapp.api

import com.bidugunapp.model.EventInfo
import retrofit2.Response
import retrofit2.http.GET

interface HomePageAPI {
    @GET("/events")
    suspend fun getEventInfo(
    ) : Response<EventInfo>
}