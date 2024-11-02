package com.example.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.presentation.databinding.ActivityMainBinding
import com.example.presentation.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavView: BottomNavigationView = binding.bottomNav
        val navController = (binding.navHostFragment.getFragment<NavHostFragment>())
            .findNavController()
        bottomNavView.setupWithNavController(navController)

        startLoadOffersAndVacancies()
        favoriteVacanciesBubble(bottomNavView)
    }

    private fun startLoadOffersAndVacancies() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadData()
            }
        }
    }

    private fun favoriteVacanciesBubble(bottomNavigationView: BottomNavigationView) {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNumberFavoriteVacancies().collect { numberFavorites ->
                    val menuItem =
                        binding.bottomNav.findViewById<BottomNavigationItemView>(R.id.navigation_favorites)
                    val badge = bottomNavigationView.getOrCreateBadge(menuItem.id)
                    badge.backgroundColor = Color.RED
                    badge.verticalOffset = 15
                    badge.number = numberFavorites
                    badge.isVisible = numberFavorites != 0
                }
            }
        }
    }


}