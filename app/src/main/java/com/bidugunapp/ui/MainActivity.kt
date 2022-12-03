package com.bidugunapp.ui

import android.Manifest.permission.CAMERA
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
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
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
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.services.FirebaseNotificationService
import com.bidugunapp.viewmodel.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bidugunapp.util.Constants
import dyte.io.uikit.DyteUIKit
import dyte.io.uikit.DyteUIKitBuilder
import dyte.io.uikit.DyteUIKitConfig
import io.dyte.core.models.DyteMeetingInfo


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var homePageViewModel : HomePageViewModel
    lateinit var navController: NavController
    private lateinit var userId : String
    private val firebaseNotificationService = FirebaseNotificationService()
    private lateinit var dyteUIKit : DyteUIKit

    val meetingInfo = DyteMeetingInfo(
        orgId = Constants.ORGNIZATION_ID,
        roomName = Constants.MEETING_ROOM_NAME,
        authToken = Constants.AUTH_TOKEN,
        enableAudio = false,
        enableVideo = false,
    )

    // pass in activity context for the SDK
    val liveStreamConfig = DyteUIKitConfig(
        activity = this,
        dyteMeetingInfo = meetingInfo,
    )

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        Firebase.messaging.subscribeToTopic("chats")

        //Guestbook Viewmodel
        val homePageRepository = HomePageRepository()
        val homePageViewModelFactory = HomePageViewModel.HomePageViewModelFactory(application,homePageRepository)
        homePageViewModel = ViewModelProvider(this,homePageViewModelFactory)[HomePageViewModel::class.java]

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
        setUpSplashScreen(splashScreen)

        fab_liveStream.setOnClickListener {
            // initialise an instance
            dyteUIKit = DyteUIKitBuilder.build(liveStreamConfig)
            dyteUIKit.loadUi()
        }

    }

    override fun onRestart() {
        super.onRestart()
        dyteUIKit.leaveRoom()
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
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    CAMERA)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(CAMERA), 1)
            } else {
                requestPermissionLauncher.launch(CAMERA)
            }
        }
    }

    private fun setUpSplashScreen(splashScreen: SplashScreen){
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (homePageViewModel.isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted)
            Toast.makeText(this, "Notifications permission denied",
                Toast.LENGTH_LONG).show()
    }
}