package com.example.domain.entities

data class Vacancy(
    val id: String,
    val lookingNumber: Int? = null,
    val lookingString: String = "",
    val title: String,
    val town: String,
    val company: String,
    val experience: String,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salaryShort: String? = null
)