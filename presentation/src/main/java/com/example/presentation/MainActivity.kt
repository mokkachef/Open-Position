package com.example.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
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
        bottomMenuNavigate(navController, bottomNavView)
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
                        binding.bottomNav.findViewById<BottomNavigationItemView>(R.id.navigation_favorites_item)
                    val badge = bottomNavigationView.getOrCreateBadge(menuItem.id)
                    badge.backgroundColor = Color.RED
                    badge.verticalOffset = 15
                    badge.number = numberFavorites
                    badge.isVisible = numberFavorites != 0
                }
            }
        }
    }

    private fun bottomMenuNavigate(
        navController: NavController,
        bottomNavView: BottomNavigationView
    ) {
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home_item -> {
                    navController.navigate(R.id.action_global_navigation_home)
                    true
                }

                R.id.navigation_favorites_item -> {
                    navController.navigate(R.id.action_global_navigation_favorites)
                    true
                }

                R.id.navigation_response_item -> {
                    navController.navigate(R.id.action_global_navigation_response)
                    true
                }

                R.id.navigation_messages_item -> {
                    navController.navigate(R.id.action_global_navigation_messages)
                    true
                }

                R.id.navigation_profile_item -> {
                    navController.navigate(R.id.action_global_navigation_profile)
                    true
                }

                else -> {
                    navController.navigate(R.id.action_global_navigation_home)
                    true
                }
            }
        }
    }


}