package com.example.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entities.VacancyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {

    @Query("SELECT * FROM VacancyEntity")
    fun getAllVacancies(): Flow<List<VacancyEntity>>

    @Update
    suspend fun updateVacancy(vararg vacancyEntity: VacancyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg vacancyEntity: VacancyEntity)

}
