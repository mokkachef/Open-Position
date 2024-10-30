package com.example.presentation.info

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentInfoVacancyBinding

class InfoVacancyFragment : Fragment() {

    private var _binding: FragmentInfoVacancyBinding? = null
    private val binding get() = _binding!!
    private val args: InfoVacancyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoText.text = "vacancy id" +
                ": \n ${args.vacancyId}"

    }
}