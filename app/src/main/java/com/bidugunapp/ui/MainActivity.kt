package com.bidugunapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
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
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Guestbook Viewmodel create
        val guestBookRepository = GuestBookRepository()
        val guestBookViewModelFactory = GuestBookViewModelFactory(guestBookRepository)
        guestBookViewModel = ViewModelProvider(this,guestBookViewModelFactory).get(GuestBookViewModel::class.java)

        //HomePage Viewmodel create
        val homePageRepository = HomePageRepository()
        val homePageViewModelFactory = HomePageViewModelFactory(homePageRepository)
        homePageViewModel = ViewModelProvider(this,homePageViewModelFactory).get(HomePageViewModel::class.java)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView.setupWithNavController(mainNavHostFragment.findNavController())
        bottomNavigationView.background = null
        bottomNavigationView.menu[2].isEnabled = false


        //Action bar implementation
        setSupportActionBar(toolBar)
        navController = findNavController(R.id.mainNavHostFragment)
        val config = AppBarConfiguration(navController.graph)
        toolBar.setupWithNavController(navController,config)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}