package com.asadbek.ieltsmyband

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IeltsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        if (prefs.contains("night_mode")) {
            val mode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
