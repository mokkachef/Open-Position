package com.example.data.remote.network

import retrofit2.http.GET

interface VacancyOfferApiService {

    @GET("raw/FgCjNE8D")
    suspend fun getVacanciesAndOffersFromSource() : NetworkResult<List<ApiResponse>>

}