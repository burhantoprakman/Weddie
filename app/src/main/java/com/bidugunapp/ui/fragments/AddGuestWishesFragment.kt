package com.bidugunapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bidugunapp.R
import com.bidugunapp.model.GuestBookResponse
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.viewmodel.GuestBookViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_addwishes.*


class AddGuestWishesFragment : Fragment(R.layout.fragment_addwishes) {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val guestBookRepository = GuestBookRepository()

    private val viewModel: GuestBookViewModel by lazy {
        val activity = requireNotNull(this.activity) {
        }
        ViewModelProvider(
            this,
            GuestBookViewModel.GuestBookViewModelFactory(activity.application, guestBookRepository)
        )[GuestBookViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if (result?.data != null) {
                        if (result.data?.extras?.get("data") != null) {
                            val bitmap = result.data?.extras?.get("data") as Bitmap
                            iv_add_wish_photo.setImageBitmap(bitmap)
                        } else {
                            Glide.with(this).load(result.data?.data.toString())
                                .into(iv_add_wish_photo)
                        }
                    }
                }
            }

        iv_add_wish_photo.setOnClickListener {
            showAlertDialog()
        }

        btn_share_wishes.setOnClickListener {
            val bitmap = iv_add_wish_photo.drawable.toBitmap()
            val description = edt_wish.text.toString()
            val title = edt_title.text.toString()
            val guestBook = GuestBookResponse(description, 0, 0, "", bitmap, title, true)
            viewModel.addWishes(guestBook)
            val action = AddGuestWishesFragmentDirections.actionAddGuestWishesFragmentToGuestBookFragment()
            findNavController().navigate(action)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle("Pick image")

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "CAMERA"
        ) { dialog, which -> openCamera() }

        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "GALLERY"
        ) { dialog, which -> openGallery() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }
}