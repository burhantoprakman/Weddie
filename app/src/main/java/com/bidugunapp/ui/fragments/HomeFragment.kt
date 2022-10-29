package com.bidugunapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bidugunapp.R
import com.bidugunapp.resources.Resources
import com.bidugunapp.viewmodel.HomePageViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var  viewModel : HomePageViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.homePageInfo.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { eventResponse ->
                    }
                }
                is Resources.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, "OPPS ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                }
                else -> {
                    // DO NOTHING
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}