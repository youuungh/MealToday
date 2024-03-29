package com.ninezero.mealtoday.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ninezero.mealtoday.R
import com.ninezero.mealtoday.databinding.ActivityMainBinding
import com.ninezero.mealtoday.utils.hide
import com.ninezero.mealtoday.utils.show
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        window.setDecorFitsSystemWindows(false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.hostFragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mealFragment, R.id.categoryFragment, R.id.searchFragment,
                R.id.detailFragment, R.id.categoryBottomSheet, R.id.detailOverViewFragment,
                R.id.aboutFragment -> {
                    setBottomNavVisibility(visible = false)
                }
                else -> setBottomNavVisibility(visible = true)
            }
        }
    }

    private fun setBottomNavVisibility(visible: Boolean) {
        if (visible) {
            binding.bottomNavigation.bringToFront()
            binding.bottomNavigation.show()
        } else {
            binding.bottomNavigation.hide()
        }
    }
}