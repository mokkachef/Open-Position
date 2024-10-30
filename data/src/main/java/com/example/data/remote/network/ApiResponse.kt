package com.example.data.remote.network

import com.example.data.entities.OfferDto
import com.example.data.entities.VacancyDto
import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "offers") val offers: List<OfferDto>,
    @Json(name = "vacancies") val vacancies: List<VacancyDto>
)