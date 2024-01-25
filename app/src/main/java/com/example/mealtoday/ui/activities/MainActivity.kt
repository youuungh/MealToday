package com.example.mealtoday.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.mealtoday.R
import com.example.mealtoday.databinding.ActivityMainBinding
import com.google.android.material.color.DynamicColors
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //DynamicColors.applyToActivityIfAvailable(this)
        window.navigationBarColor = SurfaceColors.SURFACE_2.getColor(this);
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            Navigation.findNavController(this, R.id.hostFragment)
        )
    }
}