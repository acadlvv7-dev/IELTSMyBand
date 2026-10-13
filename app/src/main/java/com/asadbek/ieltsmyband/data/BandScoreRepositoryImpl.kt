package com.asadbek.ieltsmyband.data

import com.asadbek.ieltsmyband.domain.repository.BandScoreRepository
import kotlin.math.floor
import javax.inject.Inject

class BandScoreRepositoryImpl @Inject constructor(): BandScoreRepository {

    override fun calculateOverall(
        listening: Double,
        speaking: Double,
        writing: Double,
        reading: Double
    ): Double {
        val awerage=(listening+reading+writing+speaking)/4
        return roundToIeltsBand(awerage)
    }



    private fun roundToIeltsBand(value:Double): Double{
        val whole = floor(value)
        val fraction = value - whole
        return when {
            fraction < 0.25 -> whole
            fraction < 0.75 -> whole + 0.5
            else -> whole + 1.0
        }
    }
}