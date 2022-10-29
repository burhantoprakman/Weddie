package com.bidugunapp.repository

import com.bidugunapp.api.RetrofitInstance

class HomePageRepository {
    suspend fun getHomePageInfo() = RetrofitInstance.homePageApi.getHomePageData()

}