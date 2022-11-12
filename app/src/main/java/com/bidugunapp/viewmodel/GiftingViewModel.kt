package com.bidugunapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.bidugunapp.model.Gift
import com.bidugunapp.model.GiftType
import com.bidugunapp.model.GuestBook
import com.bidugunapp.model.GuestBookResponse
import com.bidugunapp.repository.GiftingRepository
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.resources.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GiftingViewModel(application: Application, private val repository: GiftingRepository) :
    AndroidViewModel(application) {

    val giftList  : MutableLiveData<Resources<List<GiftType>>>  = MutableLiveData()

    init {
        getGiftingList()
    }

    private fun getGiftingList() = viewModelScope.launch(Dispatchers.IO) {
        giftList.postValue(Resources.Loading())
        val response  = repository.getGifting()
        giftList.postValue(handleGiftingResponse(response))

    }

    private fun handleGiftingResponse(response: Response<Gift>) : Resources<List<GiftType>>{
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse.giftTypeList)
            }
        }
        return Resources.Error(response.message())

    }




    @Suppress("UNCHECKED_CAST")
    class GiftingViewModelFactory(
        private val application: Application,
        private val repository: GiftingRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GiftingViewModel(application, repository) as T
        }
    }
}
