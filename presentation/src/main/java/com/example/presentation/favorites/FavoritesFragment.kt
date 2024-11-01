package com.example.presentation.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.databinding.FragmentFavoritesBinding
import com.example.presentation.home.HomeViewModel
import com.example.presentation.home.VacancyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFavoriteVacancies()
    }

    private fun loadFavoriteVacancies() {
        binding.rvFavorites.adapter = VacancyAdapter(
            onFavoriteClicked = {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.changeIsFavorite(vacancy = it)
                }
            },
            onItemClicked = {
                val action =
                    FavoritesFragmentDirections.actionNavigationFavoritesToInfoVacancyFragmentDirection(
                        vacancyId = it.id
                    )
                findNavController().navigate(action)
            }
        )
        subscribeVacancyRecycler()
    }

    private fun subscribeVacancyRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getFavoriteVacancies().collect {
                    (binding.rvFavorites.adapter as VacancyAdapter).submitList(
                        it.map { vacancy ->
                            if (vacancy.lookingNumber != null) {
                                vacancy.copy(
                                    lookingString =
                                    resources.getQuantityString(
                                        R.plurals.looking_number_string,
                                        vacancy.lookingNumber!!,
                                        vacancy.lookingNumber
                                    )
                                )
                            } else vacancy
                        }
                    )
                    viewNumberOfVacancies(it.size)
                }
            }
        }
    }

    private fun viewNumberOfVacancies(size: Int) {
        val numberString = resources.getQuantityString(
            R.plurals.number_vacancies,
            size,
            size
        )
        binding.favoritesNumberVacancy.text = numberString
    }
}