package com.bidugunapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bidugunapp.repository.GuestBookRepository

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(
    private val application: Application
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(application) as T
    }
}