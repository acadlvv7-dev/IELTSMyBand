package com.asadbek.ieltsmyband.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asadbek.ieltsmyband.domain.model.BandScore
import com.asadbek.ieltsmyband.domain.usecase.CalculateOverallBandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BandScoreViewModel @Inject constructor(
    private val calculateOverallBand: CalculateOverallBandUseCase
) : ViewModel() {

    private var listening: Double = 0.0
    private var speaking: Double = 0.0
    private var reading: Double = 0.0
    private var writing: Double = 0.0

    private val _bandScore = MutableLiveData<BandScore>()
    val bandScore: LiveData<BandScore> = _bandScore

    fun setReading(value: Double) {
        reading = value
        recalculate()
    }

    fun setWriting(value: Double) {
        writing = value
        recalculate()
    }

    fun setListening(value: Double) {
        listening = value
        recalculate()
    }

    fun setSpeaking(value: Double) {
        speaking = value
        recalculate()
    }

    fun recalculate() {
        _bandScore.value = calculateOverallBand(listening, reading, speaking, writing)
    }
}
