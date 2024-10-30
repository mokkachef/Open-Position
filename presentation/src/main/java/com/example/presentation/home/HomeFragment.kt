package com.example.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
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
import com.example.domain.entities.IconType
import com.example.domain.entities.Offer
import com.example.domain.entities.Vacancy
import com.example.presentation.R
import com.example.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadOffers()
        loadVacancies()
    }


    private fun loadOffers() {
        binding.rvOffers.adapter = OfferAdapter(
            onItemClicked = { offer ->
                val link = Uri.parse(offer.link)
                val intent = Intent(Intent.ACTION_VIEW, link)
                startActivity(intent)
            }
        )
        subscribeOfferRecycler()
    }

    private fun subscribeOfferRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getOffers().collect {
                    (binding.rvOffers.adapter as OfferAdapter).submitList(it)
                }
            }
        }
    }

    private fun loadVacancies() {
        binding.rvVacancies.adapter = VacancyAdapter(
            onFavoriteClicked = {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.changeIsFavorite(vacancy = it)
                }
            },
            onItemClicked = {
                val action =
                    HomeFragmentDirections.actionNavigationHomeToInfoVacancyFragmentDirection(
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
                viewModel.getVacancies().collect {
                    (binding.rvVacancies.adapter as VacancyAdapter).submitList(
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
                }
            }
        }
    }


}
