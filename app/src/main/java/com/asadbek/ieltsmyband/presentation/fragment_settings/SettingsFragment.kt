package com.asadbek.ieltsmyband.presentation.fragment_settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asadbek.ieltsmyband.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemAppearance.setOnClickListener {
            AppearanceBottomSheet.newInstance().show(
                childFragmentManager,
                AppearanceBottomSheet.TAG
            )
        }

        binding.itemAboutApp.setOnClickListener {
            AboutAppDialog.newInstance().show(
                childFragmentManager,
                AboutAppDialog.TAG
            )
        }

        binding.itemCalculationInfo.setOnClickListener {
            startActivity(Intent(requireContext(), OverallBandInfoActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
