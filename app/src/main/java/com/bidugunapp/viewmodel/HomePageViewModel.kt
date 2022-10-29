package com.bidugunapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bidugunapp.model.EventInfo
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.resources.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomePageViewModel(private val homePageRepository : HomePageRepository) : ViewModel() {
     val homePageInfo  : MutableLiveData<Resources<EventInfo>> = MutableLiveData()

    init {
        getHomePageInfo()
    }
    fun getHomePageInfo() = viewModelScope.launch(Dispatchers.IO){
        homePageInfo.postValue(Resources.Loading())
        val response = homePageRepository.getHomePageInfo()
        homePageInfo.postValue(handleHomePageInfo(response))
    }

    private fun handleHomePageInfo(response : Response<EventInfo>)
            : Resources<EventInfo> {
        if(response.isSuccessful){
            response.body()?.let{
                    resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
}