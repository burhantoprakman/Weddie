package com.bidugunapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bidugunapp.model.MessageInfo
import com.bidugunapp.model.PushNotification
import com.bidugunapp.repository.ChatRepository
import com.bidugunapp.resources.Resources
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@SuppressLint("HardwareIds")
class ChatViewModel(application: Application, repository: ChatRepository) :
    AndroidViewModel(application) {
    var messageList: MutableLiveData<Resources<List<MessageInfo>>> = MutableLiveData()
    private var userId: String?
    private var databaseReference = FirebaseDatabase.getInstance().reference
    private val repository: ChatRepository
    var list: MutableList<MessageInfo> = ArrayList()

    var pushNotificationResponse: String = ""

    init {
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")
        this.repository = repository

        //TODO Bu dogru durmuyor
        val sharedPreference =
            application.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        userId = sharedPreference.getString("userId", "")
        getDatabaseMessages()
    }

    fun createUser(message: String, time: String) = MainScope().launch(Dispatchers.IO) {
        val messageInfo = MessageInfo(time, message, userId!!)
        list.add(messageInfo)
        databaseReference.setValue(list)
    }

    private fun getDatabaseMessages() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list = ArrayList()
                for (data in snapshot.children) {
                    data.getValue(MessageInfo::class.java)?.let {
                        list.add(it)
                    }
                }
                messageList.postValue(Resources.Loading())
                messageList.postValue(Resources.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun sendNotification(pushNotification: PushNotification) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.sendNotification(pushNotification)
            pushNotificationResponse = response.message()
        }

    @Suppress("UNCHECKED_CAST")
    class ChatViewModelFactory(
        private val application: Application,
        private val repository: ChatRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(application, repository) as T
        }
    }
}

