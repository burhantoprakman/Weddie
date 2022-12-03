package com.bidugunapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bidugunapp.R
import com.bidugunapp.adapters.ChatAdapter
import com.bidugunapp.model.MessageInfo
import com.bidugunapp.model.NotificationData
import com.bidugunapp.model.PushNotification
import com.bidugunapp.repository.ChatRepository
import com.bidugunapp.resources.Resources
import com.bidugunapp.viewmodel.ChatViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chat.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ChatFragment : Fragment(R.layout.fragment_chat) {
    private var databaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var chatAdapter: ChatAdapter
    lateinit var userId: String
    private val chatRepository = ChatRepository()

    private val chatViewModel: ChatViewModel by lazy {
        val activity = requireNotNull(this.activity) {
        }
        ViewModelProvider(
            this,
            ChatViewModel.ChatViewModelFactory(activity.application, chatRepository)
        )[ChatViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatAdapter = ChatAdapter(context)
        rv_chat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(activity)
            smoothScrollToPosition(chatAdapter.itemCount)
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")

        val sharedPreference =
            context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        userId = sharedPreference?.getString("userId", "").toString()

        btn_sendMessage.setOnClickListener {
            val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            chatViewModel.createUser(et_message.text.toString(), time)
            et_message.text.clear()

            chatViewModel.sendNotification(
                PushNotification(
                    NotificationData(
                        userId,
                        "HEYY"
                    ), ""
                )
            )
        }

        ib_attachment.setOnClickListener {
            pickImage()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        chatViewModel.messageList.observe(viewLifecycleOwner) { chatResponse ->
            when (chatResponse) {
                is Resources.Success -> {
                    chatResponse.data?.let {
                        chatAdapter.chatList = it as MutableList<MessageInfo>
                        rv_chat.smoothScrollToPosition(it.size)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
                is Resources.Error -> {
                    chatResponse.message?.let {
                        Toast.makeText(activity, "OPPS ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                }
                else -> {
                }
            }
        }
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    private fun pickImage() {
    }
}