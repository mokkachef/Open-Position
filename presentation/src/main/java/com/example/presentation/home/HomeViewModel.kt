package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.Offer
import com.example.domain.entities.Vacancy
import com.example.domain.usecase.ChangeIsFavoriteUseCase
import com.example.domain.usecase.GetOffersUseCase
import com.example.domain.usecase.GetVacanciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getOffersUseCase: GetOffersUseCase,
    private val getVacanciesUseCase: GetVacanciesUseCase,
    private val changeIsFavoriteUseCase: ChangeIsFavoriteUseCase
) : ViewModel() {

    private val listOffers: MutableStateFlow<List<Offer>> = MutableStateFlow(listOf())
    private val listVacancies: MutableStateFlow<List<Vacancy>> = MutableStateFlow(listOf())
    private val listVacanciesFavorites: MutableStateFlow<List<Vacancy>> = MutableStateFlow(listOf())
    private val numberFavoriteVacancies: MutableStateFlow<Int> = MutableStateFlow(0)
    private val loadMore: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val numberOfVacancies: MutableStateFlow<Int> = MutableStateFlow(0)

    suspend fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            getOffersUseCase().collect {
                listOffers.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            getVacanciesUseCase().collect { list ->
                listVacancies.value = list
                val listFavorite = list.filter { it.isFavorite }
                listVacanciesFavorites.value = listFavorite
                numberFavoriteVacancies.value = listFavorite.size
                loadMore.value = false
                numberOfVacancies.value = list.size
            }
        }
    }

    fun getNumberOfVacancies(): StateFlow<Int> {
        return numberOfVacancies
    }

    fun getNumberFavoriteVacancies(): StateFlow<Int> {
        return numberFavoriteVacancies
    }

    fun getOffers(): StateFlow<List<Offer>> {
        return listOffers
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getVacancies(): StateFlow<List<Vacancy>> {
        return loadMore.mapLatest {
            if (it) listVacancies.value
            else listVacancies.value.take(3)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listVacancies.value
        )
    }

    suspend fun changeIsFavorite(vacancy: Vacancy) {
        changeIsFavoriteUseCase(vacancy)
    }

    fun getFavoriteVacancies(): StateFlow<List<Vacancy>> {
        return listVacanciesFavorites
    }

    fun moreVacancies(b: Boolean) {
        loadMore.value = b
    }

}