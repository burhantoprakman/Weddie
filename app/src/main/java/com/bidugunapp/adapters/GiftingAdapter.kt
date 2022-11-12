package com.bidugunapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bidugunapp.R
import com.bidugunapp.model.GiftType
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gifting_item_view.view.*

class GiftingAdapter : RecyclerView.Adapter<GiftingAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<GiftType>() {
        override fun areItemsTheSame(
            oldItem: GiftType,
            newItem: GiftType
        ): Boolean {
            return oldItem.giftId == newItem.giftId
        }

        override fun areContentsTheSame(
            oldItem: GiftType,
            newItem: GiftType
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.gifting_item_view,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((GiftType) -> Unit)? = null

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val giftResponse = differ.currentList[position]
        holder.itemView.apply {

            tv_shopName.text = giftResponse.giftName
            Glide.with(this).load(giftResponse.giftIcon).into(iv_shopIcon)

            setOnClickListener {
                onItemClickListener?.let { it(giftResponse) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (GiftType) -> Unit) {
        onItemClickListener = listener
    }
}