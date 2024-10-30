package com.example.domain.repository

import com.example.domain.entities.Vacancy
import kotlinx.coroutines.flow.Flow

interface VacanciesRepository {

    suspend fun getVacancies(): Flow<List<Vacancy>>

    suspend fun changeIsFavorite(vacancy: Vacancy)

}