package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.R
import com.example.spacelocations.databinding.FragmentLoginBinding
import com.example.spacelocations.viewmodel.ViewModel

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
        /*viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.loggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.login_to_splash)
            }
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener{
            viewModel.register("someemail", "somePassword")
        }
    }
}