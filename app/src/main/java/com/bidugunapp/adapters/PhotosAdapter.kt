package com.bidugunapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bidugunapp.R
import com.bidugunapp.model.Photo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_item_view.view.*

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(
            oldItem: Photo,
            newItem: Photo
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Photo,
            newItem: Photo
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    inner class PhotosViewHolder(itemView : View)  : ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo_item_view,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
       val photos = differ.currentList[position]
        val likedCount = photos.likedCount.toString()
        holder.itemView.apply {
            Glide.with(this).load(photos.url).into(iv_photo_item)
            tv_likesCount.text ="Liked by $likedCount"
            if(!photos.liked){
                holder.itemView.ib_likes.setImageResource(R.drawable.ic_liked)
            } else {
                holder.itemView.ib_likes.setImageResource(R.drawable.ic_like)
            }
        }
        holder.itemView.ib_likes.setOnClickListener {
            if(!photos.liked){
                photos.liked = true
                holder.itemView.ib_likes.setImageResource(R.drawable.ic_liked)
            } else {
                photos.liked = false
                holder.itemView.ib_likes.setImageResource(R.drawable.ic_like)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}