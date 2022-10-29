package com.bidugunapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bidugunapp.R
import com.bidugunapp.databinding.ActivityMainBinding
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.viewmodel.GuestBookViewModel
import com.bidugunapp.viewmodel.GuestBookViewModelFactory
import com.bidugunapp.viewmodel.HomePageViewModel
import com.bidugunapp.viewmodel.HomePageViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var guestBookViewModel: GuestBookViewModel
    lateinit var homePageViewModel : HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())
        bottomNavigationView.background = null
        bottomNavigationView.menu.get(2).isEnabled = false

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        //Guestbook Viewmodel create
        val guestBookRepository = GuestBookRepository()
        val guestBookViewModelFactory = GuestBookViewModelFactory(guestBookRepository)
        guestBookViewModel = ViewModelProvider(this,guestBookViewModelFactory).get(GuestBookViewModel::class.java)

        //HomePage Viewmodel create
        val homePageRepository = HomePageRepository()
        val homePageViewModelFactory = HomePageViewModelFactory(homePageRepository)
        homePageViewModel = ViewModelProvider(this,homePageViewModelFactory).get(HomePageViewModel::class.java)

    }
}