package com.example.domain.usecase

import com.example.domain.entities.Vacancy
import com.example.domain.repository.VacanciesRepository
import javax.inject.Inject

class ChangeIsFavoriteUseCase @Inject constructor(
    private val vacanciesRepository: VacanciesRepository
) {

    suspend operator fun invoke(vacancy: Vacancy) {
        vacanciesRepository.changeIsFavorite(vacancy)
    }

}