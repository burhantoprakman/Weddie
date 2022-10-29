package com.bidugunapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidugunapp.R
import com.bidugunapp.model.GuestBook
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.guest_book_item_view.view.*

class GuestBookAdapter() : RecyclerView.Adapter<GuestBookAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<GuestBook>(){
        override fun areItemsTheSame(oldItem: GuestBook, newItem: GuestBook): Boolean {
            return oldItem.guestId == newItem.guestId
        }

        override fun areContentsTheSame(oldItem: GuestBook, newItem: GuestBook): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.guest_book_item_view,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((GuestBook) -> Unit)? = null
    private var sortType : String = ""

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val guestBook = differ.currentList[position]
        holder.itemView.apply {
            //Glide.with(this).load(guestBook.icon).into(iv_guest_photo)
            tv_guest_name.text = guestBook.title
            tv_guest_text.text = guestBook.text
        setOnClickListener {
            onItemClickListener?.let { it(guestBook) }
            }
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (GuestBook) -> Unit) {
        onItemClickListener = listener
    }
}