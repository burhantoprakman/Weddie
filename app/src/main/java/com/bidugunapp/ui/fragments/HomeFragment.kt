package com.bidugunapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bidugunapp.R
import com.bidugunapp.adapters.HomePageViewPagerAdapter
import com.bidugunapp.resources.Resources
import com.bidugunapp.ui.MainActivity
import com.bidugunapp.util.Constants.Companion.HOME_PAGE_PHOTOS_DELAY_TIME_MS
import com.bidugunapp.viewmodel.HomePageViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.math.abs

class HomeFragment : Fragment(R.layout.fragment_home), OnMapReadyCallback {
    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler
    private lateinit var homePageViewModel : HomePageViewModel
    private lateinit var adapter: HomePageViewPagerAdapter
    private var photosList: MutableList<String> = ArrayList()
    private var googleMapLocationLat : Double = 0.0
    private var googleMapLocationLng : Double = 0.0
    private lateinit var googleMap : GoogleMap

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homePageViewModel = (activity as MainActivity).homePageViewModel
        setUpAdapter()
        setUpTransformer()
        homePageViewModel.eventInfoList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    response.data?.let { eventResponse ->
                        photosList.addAll(eventResponse.photos)
                        adapter.notifyDataSetChanged()

                        tv_name.text = eventResponse.name
                        tv_date.text = eventResponse.date
                        tv_description.text = eventResponse.text
                        tv_location.text = eventResponse.location
                        val mainMenu = eventResponse.foodMenu.anaMenu
                        val dessert = eventResponse.foodMenu.tatli
                        val soup = eventResponse.foodMenu.corba
                        val appetizer = eventResponse.foodMenu.aperatif

                        showLocation(eventResponse.gmapsLocationLat.toDouble(),
                            eventResponse.gmapsLocationLng.toDouble())

                        tv_menu.text =
                            " Main menu :$mainMenu \n Dessert : $dessert \n Soup : $soup \n Appetizer : $appetizer"
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

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, HOME_PAGE_PHOTOS_DELAY_TIME_MS)
            }
        })

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, HOME_PAGE_PHOTOS_DELAY_TIME_MS)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private val runnable = Runnable {
        viewPager.currentItem += 1
    }

    private fun setUpAdapter() {
        viewPager = viewpager_home
        adapter = HomePageViewPagerAdapter(viewPager)
        adapter.list = photosList
        handler = Handler(Looper.myLooper()!!)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        viewPager.setPageTransformer(transformer)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap  =googleMap
    }

    private fun showLocation(lat : Double , lng : Double){
        val location = LatLng(lat, lng)
        googleMap.addMarker(MarkerOptions().position(location).title(""))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.setMinZoomPreference(12f)
    }
}