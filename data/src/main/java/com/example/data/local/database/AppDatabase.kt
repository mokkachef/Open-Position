package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.entities.VacancyEntity

@Database(entities = [VacancyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun VacancyDao(): VacancyDao

}