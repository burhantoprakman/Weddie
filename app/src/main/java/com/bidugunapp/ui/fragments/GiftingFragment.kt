package com.bidugunapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bidugunapp.R
import com.bidugunapp.adapters.GiftingAdapter
import com.bidugunapp.repository.GiftingRepository
import com.bidugunapp.resources.Resources
import com.bidugunapp.viewmodel.GiftingViewModel
import kotlinx.android.synthetic.main.fragment_gifting.*


class GiftingFragment : Fragment(R.layout.fragment_gifting) {
    private val giftingRepository = GiftingRepository()
    private lateinit var giftingAdapter : GiftingAdapter

    private val giftingViewModel: GiftingViewModel by lazy {
        val activity = requireNotNull(this.activity) {
        }
        ViewModelProvider(
            this,
            GiftingViewModel.GiftingViewModelFactory(activity.application, giftingRepository)
        )[GiftingViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        giftingViewModel.giftList.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { giftingResponse ->
                        giftingAdapter.differ.submitList(giftingResponse)
                    }
                    //hideProgressBar()
                }
                is Resources.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, "OPPS ERROR", Toast.LENGTH_SHORT).show()
                        //hideProgressBar()
                    }
                }
                is Resources.Loading -> {
                   // showProgressBar()
                }
                else -> {
                    // DO NOTHING
                }
            }

        }

       giftingAdapter.setOnItemClickListener {
           val urlIntent =
               Intent(Intent.ACTION_VIEW, Uri.parse(it.giftWebUrl))
           startActivity(urlIntent)
       }
    }

    fun setUpAdapter(){
        giftingAdapter = GiftingAdapter()
        rv_gifting.apply {
            adapter = giftingAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}