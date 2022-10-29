package com.bidugunapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bidugunapp.repository.HomePageRepository

@Suppress("UNCHECKED_CAST")
class HomePageViewModelFactory(private val repository : HomePageRepository)
    : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomePageViewModel(repository) as T
    }
}