package com.bidugunapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bidugunapp.R
import com.bidugunapp.adapters.PhotosAdapter
import com.bidugunapp.model.Photo
import com.bidugunapp.repository.PhotosRepository
import com.bidugunapp.resources.Resources
import com.bidugunapp.ui.MainActivity
import com.bidugunapp.viewmodel.HomePageViewModel
import com.bidugunapp.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.android.synthetic.main.photo_item_view.view.*

class PhotosFragment : Fragment(R.layout.fragment_photos) {

    private val photosAdapter = PhotosAdapter()
    private val photosRepository = PhotosRepository()


    private val photosViewModel: PhotosViewModel by lazy {
        val activity = requireNotNull(this.activity) {
        }
        ViewModelProvider(
            this,
            PhotosViewModel.PhotosViewModelFactory(activity.application, photosRepository)
        )[PhotosViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPhotosAdapter()
        photosViewModel.photosList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { photosResponse ->
                        photosAdapter.differ.submitList(photosResponse)
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
                    //showProgressBar()
                }
                else -> {
                    // DO NOTHING
                }
            }

        }
        photosViewModel.workInfos.observe(viewLifecycleOwner) { workInfoList ->
            // get the workINfo from workInfoList
            if (!workInfoList.isNullOrEmpty()) {
                val workInfo = workInfoList[0]
                // now check workInfo's status and update UI
                if (workInfo.state.isFinished) {
                    val workerResponse = workInfo.outputData.keyValueMap["photos"]
                    photosAdapter.differ.submitList(workerResponse as MutableList<Photo>?)
                } else {
                    //showProgressBar()
                }
            }
        }

    }

    private fun setUpPhotosAdapter() {
        rv_photos.apply {
            adapter = photosAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}