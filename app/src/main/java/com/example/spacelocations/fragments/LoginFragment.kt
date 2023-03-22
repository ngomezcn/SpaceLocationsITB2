package com.example.spacelocations.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.Categories
import com.example.spacelocations.R
import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.databinding.FragmentLoginBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.viewmodel.ViewModel
import java.util.*

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.login_to_map)
        }

        binding.createAccountButton.setOnClickListener {
            findNavController().navigate(R.id.login_to_register)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.loggedIn.observe(viewLifecycleOwner){
            if(it){

                findNavController().navigate(R.id.login_to_splash)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener{
            try {
                val email : String = binding.emailLogin.text.toString()
                val password : String = binding.passwordLogin.text.toString()
                viewModel.login(email,password) // i - 123456
                ServiceLocator.configureRealm()
            } catch (ex : Exception)
            {
                Toast.makeText(activity, "User and password F", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.hide()
    }

    override fun onStop() {
        super.onStop()
        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.show()
    }
}