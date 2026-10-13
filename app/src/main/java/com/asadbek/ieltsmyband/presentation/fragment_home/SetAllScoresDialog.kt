package com.asadbek.ieltsmyband.presentation.fragment_home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.asadbek.ieltsmyband.R
import com.asadbek.ieltsmyband.databinding.DialogSetAllScoresBinding
import java.util.Locale

class SetAllScoresDialog : DialogFragment() {

    private var _binding: DialogSetAllScoresBinding? = null
    private val binding get() = _binding!!

    var onScoresSavedListener: ((Double, Double, Double, Double) -> Unit)? = null

    private var listeningScore: Double? = null
    private var readingScore: Double? = null
    private var writingScore: Double? = null
    private var speakingScore: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSetAllScoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0.65f)
        }

        updateUi()

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.cardDialogListening.setOnClickListener {
            showSingleScorePicker("Listening", listeningScore ?: 6.5) { score ->
                listeningScore = score
                updateUi()
            }
        }

        binding.cardDialogReading.setOnClickListener {
            showSingleScorePicker("Reading", readingScore ?: 6.0) { score ->
                readingScore = score
                updateUi()
            }
        }

        binding.cardDialogWriting.setOnClickListener {
            showSingleScorePicker("Writing", writingScore ?: 6.5) { score ->
                writingScore = score
                updateUi()
            }
        }

        binding.cardDialogSpeaking.setOnClickListener {
            showSingleScorePicker("Speaking", speakingScore ?: 7.0) { score ->
                speakingScore = score
                updateUi()
            }
        }

        binding.btnSaveAll.setOnClickListener {
            val l = listeningScore ?: 6.5
            val r = readingScore ?: 6.0
            val w = writingScore ?: 6.5
            val s = speakingScore ?: 7.0
            onScoresSavedListener?.invoke(l, r, w, s)
            dismiss()
        }
    }

    private fun updateUi() {
        if (listeningScore != null) {
            binding.tvDialogListeningVal.text = String.format(Locale.US, "%.1f", listeningScore)
            binding.imgDialogListeningCheck.visibility = View.VISIBLE
        } else {
            binding.tvDialogListeningVal.setText(R.string.score_range_placeholder)
            binding.imgDialogListeningCheck.visibility = View.GONE
        }

        if (readingScore != null) {
            binding.tvDialogReadingVal.text = String.format(Locale.US, "%.1f", readingScore)
            binding.imgDialogReadingCheck.visibility = View.VISIBLE
        } else {
            binding.tvDialogReadingVal.setText(R.string.score_range_placeholder)
            binding.imgDialogReadingCheck.visibility = View.GONE
        }

        if (writingScore != null) {
            binding.tvDialogWritingVal.text = String.format(Locale.US, "%.1f", writingScore)
            binding.imgDialogWritingCheck.visibility = View.VISIBLE
        } else {
            binding.tvDialogWritingVal.setText(R.string.score_range_placeholder)
            binding.imgDialogWritingCheck.visibility = View.GONE
        }

        if (speakingScore != null) {
            binding.tvDialogSpeakingVal.text = String.format(Locale.US, "%.1f", speakingScore)
            binding.imgDialogSpeakingCheck.visibility = View.VISIBLE
        } else {
            binding.tvDialogSpeakingVal.setText(R.string.score_range_placeholder)
            binding.imgDialogSpeakingCheck.visibility = View.GONE
        }
    }

    private fun showSingleScorePicker(title: String, initialScore: Double, onSaved: (Double) -> Unit) {
        val dialog = SingleScoreDialog.newInstance()
        dialog.setArguments(title, initialScore)
        dialog.onScoreSavedListener = onSaved
        dialog.show(childFragmentManager, SingleScoreDialog.TAG)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val displayWidth = resources.displayMetrics.widthPixels
            val maxAllowedWidth = (500 * resources.displayMetrics.density).toInt()
            val calculatedWidth = (displayWidth * 0.92).toInt()
            val finalWidth = Math.min(calculatedWidth, maxAllowedWidth)
            setLayout(finalWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun setInitialValues(l: Double?, r: Double?, w: Double?, s: Double?) {
        listeningScore = l
        readingScore = r
        writingScore = w
        speakingScore = s
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SetAllScoresDialog"
        fun newInstance() = SetAllScoresDialog()
    }
}
