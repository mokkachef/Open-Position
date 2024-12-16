package com.example.presentation.home

import android.content.Intent
import android.net.Uri
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
import com.example.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

        viewModel.moreVacancies(false)
        loadOffers()
        loadVacancies()
        loadMoreVacancies()
        returnBackArrow()
    }

    private fun loadMoreVacancies() {
        binding.moreVacanciesButton.setOnClickListener {
            viewModel.moreVacancies(true)
            binding.searchFieldIcon.setImageResource(R.drawable.arrow)
            binding.moreVacanciesButton.visibility = View.GONE
            binding.numberVacanciesHint.visibility = View.VISIBLE
            binding.sortType.visibility = View.VISIBLE
            binding.sortIcon.visibility = View.VISIBLE
            binding.rvOffers.visibility = View.GONE
            binding.vacancyBlockTitle.visibility = View.GONE

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getNumberOfVacancies().collect {
                        binding.numberVacanciesHint.text = resources.getQuantityString(
                            R.plurals.number_vacancies,
                            it,
                            it
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNumberOfVacancies().collect {
                    val number =  if (it >=  3 ) it - 3
                    else 0
                    binding.moreVacanciesButton.text = resources.getQuantityString(
                        R.plurals.more_vacancies,
                        number,
                        number
                    )
                }
            }
        }
    }

    private fun returnBackArrow() {
        binding.searchFieldIcon.setOnClickListener {
            binding.searchFieldIcon.setImageResource(R.drawable.search_icon)
            binding.moreVacanciesButton.visibility = View.VISIBLE
            binding.numberVacanciesHint.visibility = View.GONE
            binding.sortType.visibility = View.GONE
            binding.sortIcon.visibility = View.GONE
            binding.rvOffers.visibility = View.VISIBLE
            binding.vacancyBlockTitle.visibility = View.VISIBLE
            viewModel.moreVacancies(false)
        }

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
                    println("darova")
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
                viewModel.getVacancies().collect { listVacancies ->
                    (binding.rvVacancies.adapter as VacancyAdapter).submitList(
                        listVacancies.map { vacancy ->
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
