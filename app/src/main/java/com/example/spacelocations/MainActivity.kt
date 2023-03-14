package com.example.spacelocations

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.fragments.LoginFragment
import com.example.spacelocations.viewmodel.ViewModel
import com.google.android.material.navigation.NavigationView
import io.realm.kotlin.mongodb.App


lateinit var app: App

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<ViewModel>()

    lateinit var navHostFragment: NavHostFragment
    lateinit var navControllerDrawer: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout : DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerDrawer = navHostFragment.navController
        drawerLayout = binding.drawerLayout
        binding.navigationView.setupWithNavController(navControllerDrawer)
        appBarConfiguration = AppBarConfiguration(setOf(
           R.id.MapFragment, R.id.ReyclerViewFragment), drawerLayout)
        setupActionBarWithNavController(navControllerDrawer, appBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener(this);

        val navigationView = binding.navigationView as NavigationView
        val textView = navigationView.findViewById<TextView>(R.id.logout)
        textView.setOnClickListener {
            viewModel.logout()
            drawerLayout.close()
            navControllerDrawer.navigate(R.id.LoginFragment)
        }
        viewModel.mBinding.postValue(binding);
    }

    override fun onSupportNavigateUp(): Boolean {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navControllerDrawer = navHostFragment.navController
        return navControllerDrawer.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.allCat -> {
                viewModel.filterCategory.postValue(null)
                drawerLayout.closeDrawers();

                return true
            }
            R.id.lifOff -> {
                viewModel.filterCategory.postValue(Categories.LifOff)
                drawerLayout.closeDrawers();

                return true
            }
            R.id.primaryStage -> {
                viewModel.filterCategory.postValue(Categories.PrimaryStage)
                drawerLayout.closeDrawers();

                return true
            }
            R.id.secondaryStage -> {
                viewModel.filterCategory.postValue(Categories.SecondaryStage)
                drawerLayout.closeDrawers();

                return true
            }
            R.id.menu_MapFragment -> {
                val navHostFragment: NavHostFragment? =  supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                Navigation.findNavController(this, navHostFragment!!.childFragmentManager.fragments[0].id).navigate(R.id.MapFragment);
                drawerLayout.closeDrawers();
                return true

            }
            R.id.menu_ReyclerViewFragment -> {
                val navHostFragment: NavHostFragment? =  supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                Navigation.findNavController(this, navHostFragment!!.childFragmentManager.fragments[0].id).navigate(R.id.ReyclerViewFragment);
                drawerLayout.closeDrawers();
                return true

            }
            else -> return true
        }
        return true
    }
}