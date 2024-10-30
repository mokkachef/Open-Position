package com.example.data.entities

import com.squareup.moshi.Json


data class VacancyDto(
    @Json(name = "id") val id: String,
    @Json(name = "lookingNumber") val lookingNumber: Int? = null,
    @Json(name = "title") val title: String,
    @Json(name = "address") val address: Address,
    @Json(name = "company") val company: String,
    @Json(name = "experience") val experience: Experience,
    @Json(name = "publishedDate") val publishedDate: String,
    @Json(name = "isFavorite") val isFavorite: Boolean,
    @Json(name = "salary") val salary: Salary
)

data class Address(
    @Json(name = "town") val town: String,
    @Json(name = "street") val street: String,
    @Json(name = "house") val house: String,
)

data class Experience(
    @Json(name = "previewText") val previewText: String,
    @Json(name = "text") val text: String
)

data class Salary(
    @Json(name = "full") val full: String,
    @Json(name = "short") val short: String? = null
)

fun VacancyDto.toVacancyEntity(): VacancyEntity = VacancyEntity(
    id = id,
    lookingNumber = lookingNumber,
    title = title,
    town = address.town,
    company = company,
    experience = experience.previewText,
    publishedDate = publishedDate,
    isFavorite = isFavorite,
    salaryShort = salary.short
)