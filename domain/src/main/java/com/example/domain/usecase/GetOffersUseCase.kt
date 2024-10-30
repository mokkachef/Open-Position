package com.example.domain.usecase

import com.example.domain.entities.Offer
import com.example.domain.repository.OffersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOffersUseCase @Inject constructor(
    private val offersRepository: OffersRepository
) {

    suspend operator fun invoke(): Flow<List<Offer>> {
        return offersRepository.getOffers()
    }

}