package com.bidugunapp

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MyApplication : Application() {
    @SuppressLint("HardwareIds")
    override fun onCreate() {
        Firebase.initialize(this)
        super.onCreate()
    }

}