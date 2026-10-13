package com.asadbek.ieltsmyband.presentation.fragment_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.asadbek.ieltsmyband.R
import com.asadbek.ieltsmyband.data.BandScoreRepositoryImpl
import com.asadbek.ieltsmyband.data.local.AppDatabase
import com.asadbek.ieltsmyband.data.local.HistoryEntity
import com.asadbek.ieltsmyband.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val repository = BandScoreRepositoryImpl()

    private var listeningScore: Double? = null
    private var readingScore: Double? = null
    private var writingScore: Double? = null
    private var speakingScore: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInfo.setOnClickListener {
            showAboutDialog()
        }

        binding.cardListening.setOnClickListener {
            showSingleScoreDialog("Listening", listeningScore ?: 6.5) { score ->
                listeningScore = score
                binding.tvListeningScore.text = String.format(Locale.US, "%.1f", score)
                binding.imgListeningCheck.visibility = View.VISIBLE
                calculateOverallIfReady()
            }
        }

        binding.cardReading.setOnClickListener {
            showSingleScoreDialog("Reading", readingScore ?: 6.0) { score ->
                readingScore = score
                binding.tvReadingScore.text = String.format(Locale.US, "%.1f", score)
                binding.imgReadingCheck.visibility = View.VISIBLE
                calculateOverallIfReady()
            }
        }

        binding.cardWriting.setOnClickListener {
            showSingleScoreDialog("Writing", writingScore ?: 6.5) { score ->
                writingScore = score
                binding.tvWritingScore.text = String.format(Locale.US, "%.1f", score)
                binding.imgWritingCheck.visibility = View.VISIBLE
                calculateOverallIfReady()
            }
        }

        binding.cardSpeaking.setOnClickListener {
            showSingleScoreDialog("Speaking", speakingScore ?: 7.0) { score ->
                speakingScore = score
                binding.tvSpeakingScore.text = String.format(Locale.US, "%.1f", score)
                binding.imgSpeakingCheck.visibility = View.VISIBLE
                calculateOverallIfReady()
            }
        }

        binding.cardSetGoal.setOnClickListener {
            val dialog = SetAllScoresDialog.newInstance()
            dialog.setInitialValues(
                listeningScore,
                readingScore,
                writingScore,
                speakingScore
            )
            dialog.onScoresSavedListener = { l, r, w, s ->
                listeningScore = l
                readingScore = r
                writingScore = w
                speakingScore = s

                binding.tvListeningScore.text = String.format(Locale.US, "%.1f", l)
                binding.imgListeningCheck.visibility = View.VISIBLE

                binding.tvReadingScore.text = String.format(Locale.US, "%.1f", r)
                binding.imgReadingCheck.visibility = View.VISIBLE

                binding.tvWritingScore.text = String.format(Locale.US, "%.1f", w)
                binding.imgWritingCheck.visibility = View.VISIBLE

                binding.tvSpeakingScore.text = String.format(Locale.US, "%.1f", s)
                binding.imgSpeakingCheck.visibility = View.VISIBLE

                calculateOverallIfReady()
            }
            dialog.show(childFragmentManager, SetAllScoresDialog.TAG)
        }
    }

    private fun showSingleScoreDialog(title: String, initialScore: Double, onSaved: (Double) -> Unit) {
        val dialog = SingleScoreDialog.newInstance()
        dialog.setArguments(title, initialScore)
        dialog.onScoreSavedListener = onSaved
        dialog.show(childFragmentManager, SingleScoreDialog.TAG)
    }

    private fun calculateOverallIfReady() {
        val l = listeningScore
        val r = readingScore
        val w = writingScore
        val s = speakingScore

        updateGoalCardState()

        if (l != null && r != null && w != null && s != null) {
            val overall = repository.calculateOverall(
                listening = l,
                speaking = s,
                writing = w,
                reading = r
            )
            binding.tvOverallScore.text = String.format(Locale.US, "%.1f", overall)
            saveToHistory(overall, l, r, w, s)
        }
    }

    private fun updateGoalCardState() {
        val hasScores = listeningScore != null || readingScore != null || writingScore != null || speakingScore != null
        if (hasScores) {
            binding.iconTargetBg.setImageResource(R.drawable.ic_edit_blue)
            binding.tvGoalTitle.setText(R.string.update_your_scores)
            binding.tvGoalSubtitle.setText(R.string.tap_to_edit_subtitle)
        } else {
            binding.iconTargetBg.setImageResource(R.drawable.ic_document)
            binding.tvGoalTitle.setText(R.string.enter_your_band_scores)
            binding.tvGoalSubtitle.setText(R.string.add_your_current_score_subtitle)
        }
    }

    private fun saveToHistory(overall: Double, l: Double, r: Double, w: Double, s: Double) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        val currentDate = dateFormat.format(Date())
        val formattedOverall = String.format(Locale.US, "%.1f", overall)
        val titleText = String.format(Locale.US, "L: %.1f | R: %.1f | W: %.1f | S: %.1f", l, r, w, s)

        lifecycleScope.launch(Dispatchers.IO) {
            val ctx = context ?: return@launch
            val dao = AppDatabase.getDatabase(ctx.applicationContext).historyDao()
            dao.insert(
                HistoryEntity(
                    date = currentDate,
                    title = titleText,
                    score = formattedOverall
                )
            )
            dao.trimOldHistory()
        }
    }

    private fun showAboutDialog() {
        AboutIeltsDialog.newInstance().show(childFragmentManager, AboutIeltsDialog.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
