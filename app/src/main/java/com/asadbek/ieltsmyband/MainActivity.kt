package com.asadbek.ieltsmyband

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.asadbek.ieltsmyband.databinding.ActivityMainBinding
import com.asadbek.ieltsmyband.presentation.fragment_history.HistoryFragment
import com.asadbek.ieltsmyband.presentation.fragment_home.HomeFragment
import com.asadbek.ieltsmyband.presentation.fragment_settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val snapshot = ThemeTransition.snapshot
        if (snapshot != null) {
            val overlayView = ImageView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.FIT_XY
                setImageBitmap(snapshot)
            }
            val decorView = window.decorView as ViewGroup
            decorView.addView(overlayView)

            overlayView.animate()
                .alpha(0f)
                .setStartDelay(60)
                .setDuration(350)
                .withEndAction {
                    decorView.removeView(overlayView)
                    ThemeTransition.snapshot = null
                }
                .start()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top,systemBars.right, 0)
            insets
        }

        binding.menusAllFragments.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> show(HomeFragment.newInstance())
                R.id.history_menu -> show(HistoryFragment.newInstance())
                R.id.settings_menu -> show(SettingsFragment.newInstance())
            }
            true
        }

        if (savedInstanceState == null) {
            binding.menusAllFragments.selectedItemId = R.id.home_menu
        }
    }

    private fun show(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
