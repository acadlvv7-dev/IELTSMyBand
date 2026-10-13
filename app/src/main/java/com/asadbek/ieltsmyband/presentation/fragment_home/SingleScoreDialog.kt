package com.asadbek.ieltsmyband.presentation.fragment_home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.asadbek.ieltsmyband.databinding.DialogSingleScoreBinding
import java.util.Locale

class SingleScoreDialog : DialogFragment() {

    private var _binding: DialogSingleScoreBinding? = null
    private val binding get() = _binding!!

    var onScoreSavedListener: ((Double) -> Unit)? = null

    private val scoreValues = arrayOf(
        "0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5",
        "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0"
    )

    private var moduleTitle: String = "Score"
    private var initialScore: Double = 6.5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSingleScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0.65f)
        }

        binding.tvSingleDialogTitle.text = moduleTitle

        binding.pickerSingleScore.apply {
            minValue = 0
            maxValue = scoreValues.size - 1
            displayedValues = scoreValues
            wrapSelectorWheel = false

            val formatted = String.format(Locale.US, "%.1f", initialScore)
            val index = scoreValues.indexOf(formatted)
            value = if (index >= 0) index else scoreValues.indexOf("6.5")
        }

        binding.btnSaveSingle.setOnClickListener {
            val selectedScore = scoreValues[binding.pickerSingleScore.value].toDouble()
            onScoreSavedListener?.invoke(selectedScore)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.88).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setArguments(title: String, score: Double) {
        moduleTitle = title
        initialScore = score
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SingleScoreDialog"
        fun newInstance() = SingleScoreDialog()
    }
}
