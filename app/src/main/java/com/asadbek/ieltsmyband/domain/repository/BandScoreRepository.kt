package com.asadbek.ieltsmyband.domain.repository

interface BandScoreRepository {

    fun calculateOverall(
        listening: Double,
        speaking: Double,
        writing: Double,
        reading: Double
    ): Double
}