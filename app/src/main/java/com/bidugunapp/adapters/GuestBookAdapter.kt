package com.bidugunapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidugunapp.R
import com.bidugunapp.model.GuestBookResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.guest_book_item_view.view.*

class GuestBookAdapter() : RecyclerView.Adapter<GuestBookAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<GuestBookResponse>() {
        override fun areItemsTheSame(
            oldItem: GuestBookResponse,
            newItem: GuestBookResponse
        ): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(
            oldItem: GuestBookResponse,
            newItem: GuestBookResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.guest_book_item_view,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((GuestBookResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val guestBookResponse = differ.currentList[position]
        holder.itemView.apply {

            tv_guest_name.text = guestBookResponse.title
            tv_guest_text.text = guestBookResponse.description
            Glide.with(this).load(guestBookResponse.photoUrl).into(iv_guest_photo)

            setOnClickListener {
                onItemClickListener?.let { it(guestBookResponse) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (GuestBookResponse) -> Unit) {
        onItemClickListener = listener
    }
}