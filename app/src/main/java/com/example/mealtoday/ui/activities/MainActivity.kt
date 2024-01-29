package com.example.mealtoday.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mealtoday.R
import com.example.mealtoday.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //DynamicColors.applyToActivityIfAvailable(this)
        //window.navigationBarColor = SurfaceColors.SURFACE_2.getColor(this);
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mealFragment -> binding.bottomNavigation.apply {
                    binding.bottomNavigation.isVisible = false
                    binding.bottomNavigation.animate()
                        .setDuration(300L)
                        .translationY(binding.bottomNavigation.height.toFloat())
                }
                else -> binding.bottomNavigation.apply {
                    binding.bottomNavigation.bringToFront()
                    binding.bottomNavigation.isVisible = true
                    binding.bottomNavigation.animate()
                        .translationY(0f)
                        .setDuration(350L)
                }
            }
        }
    }
}