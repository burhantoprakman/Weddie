package com.bidugunapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bidugunapp.model.GuestBook
import com.bidugunapp.model.GuestBookResponse
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.resources.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GuestBookViewModel(application: Application, private val repository: GuestBookRepository) :
    AndroidViewModel(application) {

    val guestBookList: MutableLiveData<Resources<List<GuestBookResponse>>> = MutableLiveData()

    init {
        getGuestBookList()
    }

    private fun getGuestBookList() = viewModelScope.launch(Dispatchers.IO) {
        guestBookList.postValue(Resources.Loading())
        val response = repository.getGuestBookList()
        guestBookList.postValue(handleGuestBookResponse(response))
    }

    private fun handleGuestBookResponse(response: Response<GuestBook>)
            : Resources<List<GuestBookResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse.guestBookList)
            }
        }
        return Resources.Error(response.message())
    }

    fun addWishes(guestBookResponse: GuestBookResponse) = viewModelScope.launch(Dispatchers.IO) {
      repository.addWishes(guestBookResponse)
    }

    @Suppress("UNCHECKED_CAST")
    class GuestBookViewModelFactory(
        private val application: Application,
        private val repository: GuestBookRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GuestBookViewModel(application, repository) as T
        }
    }
}