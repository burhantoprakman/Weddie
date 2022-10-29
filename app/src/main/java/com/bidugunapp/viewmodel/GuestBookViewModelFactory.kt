package com.bidugunapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bidugunapp.repository.GuestBookRepository

@Suppress("UNCHECKED_CAST")
class GuestBookViewModelFactory(
    private val repository: GuestBookRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GuestBookViewModel(repository) as T
    }

}