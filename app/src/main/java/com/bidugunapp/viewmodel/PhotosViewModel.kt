package com.bidugunapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bidugunapp.model.Photo
import com.bidugunapp.model.PhotosResponse
import com.bidugunapp.repository.PhotosRepository
import com.bidugunapp.resources.Resources
import com.bidugunapp.worker.RefreshDataWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotosViewModel(application: Application, private val repository: PhotosRepository) :
    AndroidViewModel(application) {
    val photosList: MutableLiveData<Resources<List<Photo>>> = MutableLiveData()
    private val workManager = WorkManager.getInstance(application)
    internal val workInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData("photos")

    init {
        getPhotos()
    }

    private fun getPhotos() = viewModelScope.launch(Dispatchers.IO) {
        photosList.postValue(Resources.Loading())
        val response = repository.getPhotos()
        photosList.postValue(handlePhotosResponse(response))

    }

    private fun handlePhotosResponse(response: Response<PhotosResponse>)
            : Resources<List<Photo>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse.photos)
            }
        }
        return Resources.Error(response.message())
    }

    @Suppress("UNCHECKED_CAST")
    class PhotosViewModelFactory(
        private val application: Application,
        private val repository: PhotosRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PhotosViewModel(application, repository) as T
        }
    }
}