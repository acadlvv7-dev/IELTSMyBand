package com.asadbek.ieltsmyband.domain.usecase

import com.asadbek.ieltsmyband.domain.model.BandScore
import com.asadbek.ieltsmyband.domain.repository.BandScoreRepository
import javax.inject.Inject

class CalculateOverallBandUseCase @Inject constructor(private val repository: BandScoreRepository) {
    operator fun invoke(
        listening: Double,
        reading: Double,
        speaking: Double,
        writing: Double
    ): BandScore {

        val overall=repository.calculateOverall(listening,speaking,writing,reading)
        return BandScore(
            listening=listening,
            speaking = speaking,
            writing = writing,
            reading = reading,
            overall = overall
        )
    }
}