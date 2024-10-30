package com.example.data.entities

import com.example.domain.entities.IconType
import com.example.domain.entities.Offer
import com.squareup.moshi.Json
import java.util.UUID

data class OfferDto(
    @Json(name = "id") val iconType: String? = null,
    @Json(name = "title") val title: String,
    @Json(name = "link") val link: String,
    @Json(name = "button") val button: OfferButton? = null
)

data class OfferButton(
    @Json(name = "text") val text: String
)

fun OfferDto.toOffer(): Offer = Offer(
    id = UUID.randomUUID().toString(),
    iconType = when (iconType) {
        "level_up_resume" -> IconType.LevelUpResume
        "near_vacancies" -> IconType.NearVacancies
        "temporary_job" -> IconType.TemporaryJob
        else -> null
    },
    title = title.trim(),
    link = link,
    buttonOffer = button?.text
)