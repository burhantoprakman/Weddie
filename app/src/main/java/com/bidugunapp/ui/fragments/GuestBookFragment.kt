package com.bidugunapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bidugunapp.R
import com.bidugunapp.adapters.GuestBookAdapter
import com.bidugunapp.resources.Resources
import com.bidugunapp.ui.MainActivity
import com.bidugunapp.viewmodel.GuestBookViewModel
import kotlinx.android.synthetic.main.fragment_guest_book.*

class GuestBookFragment : Fragment(R.layout.fragment_guest_book) {
    private lateinit var viewModel : GuestBookViewModel
    lateinit var guestBookAdapter  : GuestBookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).guestBookViewModel
        setUpAdapter()
        viewModel.guestBookList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { guestBookResponse ->
                        guestBookAdapter.differ.submitList(guestBookResponse)
                    }
                    hideProgressBar()
                }
                is Resources.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, "OPPS ERROR", Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
                else -> {
                    // DO NOTHING
                }
            }


        }

        //TODO : Simdilik swipe refresh kalsin
//        swipe_refresh.setOnRefreshListener {
//          swipe_refresh.isRefreshing = false
//            viewModel.getGuestBookList()
//        }

        guestBookAdapter.setOnItemClickListener {
            //OPEN DETAIL SCREEN
        }

    }
    private fun setUpAdapter(){
       guestBookAdapter = GuestBookAdapter()
        rv_guest_book.apply {
            adapter = guestBookAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }
}