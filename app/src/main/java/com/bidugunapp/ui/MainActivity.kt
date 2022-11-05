package com.bidugunapp.ui

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.bidugunapp.R
import com.bidugunapp.databinding.ActivityMainBinding
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.services.FirebaseNotificationService
import com.bidugunapp.viewmodel.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var guestBookViewModel: GuestBookViewModel
    lateinit var homePageViewModel : HomePageViewModel
    lateinit var chatViewModel : ChatViewModel
    lateinit var navController: NavController
    private lateinit var userId : String
    private val firebaseNotificationService = FirebaseNotificationService()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted)
            Toast.makeText(this, "Notifications permission denied",
                Toast.LENGTH_LONG).show()
    }

    @SuppressLint("HardwareIds")
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

        //ChatViewModel
        val chatViewModelFactory = ChatViewModelFactory(application)
        chatViewModel = ViewModelProvider(this,chatViewModelFactory).get(ChatViewModel::class.java)

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

        userId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        //Sharedpreferences for keep phone uniqe id
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("userId",userId)
        editor.apply()

        getToken()
        askNotificationPermission()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            firebaseNotificationService.onNewToken(token)
        })
    }

    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

}