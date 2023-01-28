package com.effectiveapps.app.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.effectiveapps.app.R
import com.effectiveapps.app.ui.navigation.setMainNavigationGraph
import com.effectiveapps.app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        hideSplashScreen()

        setupView()

        setupNavigation()


    }

    private fun hideSplashScreen() {
        window?.setBackgroundDrawableResource(R.color.background)
        window?.navigationBarColor = resources.getColor(R.color.background,null)
    }

    private fun setupView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupNavigation() {
        navController = binding.navHost.getFragment<NavHostFragment>().navController

        navController.setMainNavigationGraph()

    }

}

