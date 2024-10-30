package com.example.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VacancyEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val lookingNumber: Int?,
    @ColumnInfo val title: String,
    @ColumnInfo val town: String,
    @ColumnInfo val company: String,
    @ColumnInfo val experience: String,
    @ColumnInfo val publishedDate: String,
    @ColumnInfo val isFavorite: Boolean,
    @ColumnInfo val salaryShort: String?
)