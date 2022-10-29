package com.bidugunapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bidugunapp.R

class PhotosFragment : Fragment(R.layout.fragment_photos) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context,"PHOTOS", Toast.LENGTH_SHORT).show()
        return super.onCreateView(inflater, container, savedInstanceState)

    }
}