package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.R
import com.example.spacelocations.viewmodel.SplashViewModel

class SplashFragment : Fragment() {
    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return   inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        viewModel.loggedIn.observe(viewLifecycleOwner){loggedIn ->
            if(loggedIn){
                findNavController().navigate(R.id.splash_to_map)
            } else {
                findNavController().navigate(R.id.splash_to_login)
            }
        }
        viewModel.start()
    }
}