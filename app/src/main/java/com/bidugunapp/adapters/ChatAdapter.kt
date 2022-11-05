package com.bidugunapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bidugunapp.R
import com.bidugunapp.model.MessageInfo
import kotlinx.android.synthetic.main.message_item.view.*
import kotlinx.android.synthetic.main.message_owner_item.view.*

class ChatAdapter(context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var chatList : MutableList<MessageInfo> = ArrayList()
    private val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    private val userId = sharedPreference?.getString("userId","")

    inner class OwnerMessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        return if(viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_owner_item,parent,false)
            OwnerMessageViewHolder(view)
        } else {
            view= LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false)
            MessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){

            is OwnerMessageViewHolder -> holder.itemView.apply {

                tv_ownerMessageText.text  = chatList[position].message
                tv_ownerMessageTime.text = chatList[position].time
            }
            is MessageViewHolder -> holder.itemView.apply {
                tv_messageText.text = chatList[position].message
                tv_messageTime.text = chatList[position].time
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if(chatList[position].userId == userId){
            TYPE_OWNER
        } else {
            TYPE_NOTOWNER
        }
    }

    companion object {
        private const val TYPE_OWNER = 0
        private const val TYPE_NOTOWNER = 1
    }
}