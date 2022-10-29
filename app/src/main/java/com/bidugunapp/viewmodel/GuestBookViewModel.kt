package com.bidugunapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bidugunapp.model.GuestBook
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.resources.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GuestBookViewModel(private val repository: GuestBookRepository) : ViewModel() {

        val guestBookList  : MutableLiveData<Resources<List<GuestBook>>> = MutableLiveData()

    init {
        getGuestBookList()
    }
    fun getGuestBookList() = viewModelScope.launch(Dispatchers.IO){
        guestBookList.postValue(Resources.Loading())
        val response = repository.getGuestBookList()
        guestBookList.postValue(handleGuestBookResponse(response))
    }

    private fun handleGuestBookResponse(response : Response<List<GuestBook>>)
    : Resources<List<GuestBook>>{
        if(response.isSuccessful){
            response.body()?.let{
                resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }


}