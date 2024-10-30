package com.example.domain.entities


data class Offer(
    val id: String,
    val iconType: IconType?,
    val title: String,
    val link: String,
    val buttonOffer: String?
)

enum class IconType {
    NearVacancies,
    LevelUpResume,
    TemporaryJob
}