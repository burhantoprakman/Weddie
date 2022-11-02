package com.bidugunapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bidugunapp.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.viewpager_item.view.*

class HomePageViewPagerAdapter(private val viewPager : ViewPager2) :
    RecyclerView.Adapter<HomePageViewPagerAdapter.PhotosViewHolder>() {

    var list : MutableList<String> = ArrayList()

    inner class PhotosViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item,parent,false)
        return PhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(this).load(list[position]).into(iv_photos)
        }
        if(position == list.size -1){
            viewPager.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private val runnable = Runnable{
        list.addAll(list)
        notifyDataSetChanged()
    }
}