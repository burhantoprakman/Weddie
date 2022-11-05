package com.bidugunapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bidugunapp.MyApplication
import com.bidugunapp.model.MessageInfo
import com.bidugunapp.resources.Resources
import com.bidugunapp.services.FirebaseNotificationService
import com.bidugunapp.ui.MainActivity
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@SuppressLint("HardwareIds")
class ChatViewModel(application: Application) : ViewModel() {
    var messageList: MutableLiveData<Resources<List<MessageInfo>>> = MutableLiveData()
    private var userId: String?
    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var application: Application
    var list: MutableList<MessageInfo> = ArrayList()

    init {
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")
        this.application = application
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
}