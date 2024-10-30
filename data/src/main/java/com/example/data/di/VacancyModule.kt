package com.example.data.di

import com.example.data.VacanciesOffersRepositoryImpl
import com.example.domain.repository.OffersRepository
import com.example.domain.repository.VacanciesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VacancyModule {

    @Binds
    @Singleton
    abstract fun bindVacanciesRepository(impl: VacanciesOffersRepositoryImpl): VacanciesRepository

    @Binds
    @Singleton
    abstract fun bindOffersRepository(impl: VacanciesOffersRepositoryImpl): OffersRepository

}