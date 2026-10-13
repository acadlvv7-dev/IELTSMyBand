package com.asadbek.ieltsmyband.presentation.fragment_settings

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.asadbek.ieltsmyband.R
import com.asadbek.ieltsmyband.ThemeTransition
import com.asadbek.ieltsmyband.databinding.BottomSheetAppearanceBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppearanceBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAppearanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        updateUi(isDarkMode)

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.cardLightMode.setOnClickListener {
            if (isDarkMode) {
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                dismiss()
            }
        }

        binding.cardDarkMode.setOnClickListener {
            if (!isDarkMode) {
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                dismiss()
            }
        }
    }

    private fun setNightMode(mode: Int) {
        val act = activity ?: return
        val ctx = act.applicationContext
        val prefs = ctx.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        prefs.edit { putInt("night_mode", mode) }

        dismiss()

        Handler(Looper.getMainLooper()).postDelayed({
            val hostActivity = activity ?: act
            val decorView = hostActivity.window?.decorView
            if (decorView != null && decorView.width > 0 && decorView.height > 0) {
                try {
                    val bitmap = Bitmap.createBitmap(
                        decorView.width,
                        decorView.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    decorView.draw(canvas)
                    ThemeTransition.snapshot = bitmap
                } catch (e: Exception) {
                    ThemeTransition.snapshot = null
                }
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }, 250)
    }

    private fun updateUi(isDarkMode: Boolean) {
        val context = context ?: return
        val selectedBg = ContextCompat.getColor(context, R.color.card_listening_bg)
        val unselectedBg = ContextCompat.getColor(context, R.color.card_bg)
        val selectedStroke = ContextCompat.getColor(context, R.color.primary_blue)
        val unselectedStroke = ContextCompat.getColor(context, R.color.stroke_color)

        if (isDarkMode) {
            binding.cardLightMode.apply {
                setCardBackgroundColor(unselectedBg)
                strokeColor = unselectedStroke
            }
            binding.radioLight.setImageResource(R.drawable.ic_radio_unchecked)

            binding.cardDarkMode.apply {
                setCardBackgroundColor(selectedBg)
                strokeColor = selectedStroke
            }
            binding.radioDark.setImageResource(R.drawable.ic_radio_checked)
        } else {
            binding.cardLightMode.apply {
                setCardBackgroundColor(selectedBg)
                strokeColor = selectedStroke
            }
            binding.radioLight.setImageResource(R.drawable.ic_radio_checked)

            binding.cardDarkMode.apply {
                setCardBackgroundColor(unselectedBg)
                strokeColor = unselectedStroke
            }
            binding.radioDark.setImageResource(R.drawable.ic_radio_unchecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AppearanceBottomSheet"
        fun newInstance() = AppearanceBottomSheet()
    }
}
