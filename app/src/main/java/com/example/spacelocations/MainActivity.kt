package com.example.spacelocations


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.viewmodel.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    //private val viewModel: ViewModel by activityViewModels()
    val viewModel by viewModels<ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.mBinding.postValue(binding);
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
                item ->
            when(item.itemId) {
                R.id.allCat -> {
                    viewModel.filterCategory.postValue(null)

                    true
                }
                R.id.lifOff -> {
                    viewModel.filterCategory.postValue(Categories.LifOff)

                    true
                }
                R.id.primaryStage -> {
                    viewModel.filterCategory.postValue(Categories.PrimaryStage)

                    true
                }
                R.id.secondaryStage -> {
                    viewModel.filterCategory.postValue(Categories.SecondaryStage)

                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.fragment_container_view)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.DetailFragment) {
                binding.bottomNavigation.visibility = View.GONE;
            } else if (destination.id == R.id.AddMarkerFragment) {
                binding.bottomNavigation.visibility = View.GONE;
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE;
            }
        }
    }



}