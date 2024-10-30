package com.example.data

import android.util.Log
import com.example.data.entities.VacancyEntity
import com.example.data.entities.toOffer
import com.example.data.entities.toVacancyEntity
import com.example.data.local.database.VacancyDao
import com.example.data.mappers.toDomain
import com.example.data.mappers.toEntity
import com.example.data.remote.network.NetworkResult
import com.example.data.remote.network.VacancyOfferApiService
import com.example.domain.entities.Offer
import com.example.domain.entities.Vacancy
import com.example.domain.repository.OffersRepository
import com.example.domain.repository.VacanciesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VacanciesOffersRepositoryImpl @Inject constructor(
    private val vacancyOfferApiService: VacancyOfferApiService,
    private val vacancyDao: VacancyDao
) : VacanciesRepository, OffersRepository {

    private val offers: MutableStateFlow<List<Offer>> = MutableStateFlow(emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadOffersVacanciesFromRemote()
        }
    }

    private suspend fun loadOffersVacanciesFromRemote() {
        when (val response = vacancyOfferApiService.getVacanciesAndOffersFromSource()) {
            is NetworkResult.Success -> {
                val list: List<VacancyEntity> =
                    response.data.flatMap { list ->
                        offers.value = list.offers.map { it.toOffer() }
                        list.vacancies.map { it.toVacancyEntity() }
                    }
                list.forEach { vacancyDao.insertAll(it) }
                Log.d("tagtagtag", " Success")
            }

            is NetworkResult.Error -> Log.d(
                "tagtagtag",
                "Error ${response.responseError}"
            )

            is NetworkResult.Exception -> Log.d(
                "tagtagtag", "Exception ${response.e}"
            )
        }
    }

    override suspend fun getVacancies(): Flow<List<Vacancy>> {
        return vacancyDao.getAllVacancies().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun changeIsFavorite(vacancy: Vacancy) {
        withContext(Dispatchers.IO) {
            vacancyDao.updateVacancy(vacancy.toEntity())
        }
    }

    override suspend fun getOffers(): Flow<List<Offer>> {
        return offers
    }

}