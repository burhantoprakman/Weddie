package com.bidugunapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bidugunapp.MyApplication
import com.bidugunapp.model.EventInfo
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.resources.Resources
import com.bidugunapp.worker.RefreshDataWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class HomePageViewModel(
    application: Application,
    private val homePageRepository: HomePageRepository
) : AndroidViewModel(application) {
    val eventInfoList: MutableLiveData<Resources<EventInfo>> = MutableLiveData()
    var isReady = false



    init {
        getEventsInfo()
    }

    private fun getEventsInfo() = viewModelScope.launch(Dispatchers.IO) {
        eventInfoList.postValue(Resources.Loading())
        val response = homePageRepository.getEventInfo()
        eventInfoList.postValue(handleEventInfo(response))
        isReady = true
    }

    private fun handleEventInfo(response: Response<EventInfo>)
            : Resources<EventInfo> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    @Suppress("UNCHECKED_CAST")
    class HomePageViewModelFactory(
        val application: Application,
        private val repository: HomePageRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomePageViewModel(application, repository) as T
        }
    }
}
