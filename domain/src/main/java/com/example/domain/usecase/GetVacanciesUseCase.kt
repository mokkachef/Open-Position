package com.example.domain.usecase

import com.example.domain.entities.Vacancy
import com.example.domain.repository.VacanciesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVacanciesUseCase @Inject constructor(
    private val vacanciesRepository: VacanciesRepository
) {

    suspend operator fun invoke(): Flow<List<Vacancy>> {
        return vacanciesRepository.getVacancies()
    }


}