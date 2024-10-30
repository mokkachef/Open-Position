package com.example.data.di

import com.example.data.remote.network.NetworkResultCallAdapterFactory
import com.example.data.remote.network.VacancyOfferApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

object NetworkModule {
    private const val BASE_URL = "https://pastebin.com/"

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideMoshi(): Moshi {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        @Provides
        @Singleton
        fun provideVacancyOfferApi(moshi: Moshi): VacancyOfferApiService {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(VacancyOfferApiService::class.java)
        }
    }

}