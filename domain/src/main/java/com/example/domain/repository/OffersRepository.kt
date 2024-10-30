package com.example.domain.repository

import com.example.domain.entities.Offer
import kotlinx.coroutines.flow.Flow

interface OffersRepository {

    suspend fun getOffers(): Flow<List<Offer>>

}