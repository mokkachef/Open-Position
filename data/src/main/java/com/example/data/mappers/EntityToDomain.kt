package com.example.data.mappers

import com.example.data.entities.VacancyEntity
import com.example.domain.entities.Vacancy

internal fun VacancyEntity.toDomain(): Vacancy = Vacancy(
    id = id,
    lookingNumber = lookingNumber,
    title = title,
    town = town,
    company = company,
    experience = experience,
    publishedDate = publishedDate,
    isFavorite = isFavorite,
    salaryShort = salaryShort
)