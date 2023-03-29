package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.R
import com.example.spacelocations.databinding.FragmentDetailBinding
import com.example.spacelocations.databinding.FragmentLoginBinding
import com.example.spacelocations.databinding.FragmentRegisterBinding
import com.example.spacelocations.viewmodel.ViewModel

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        viewModel.loggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.register_to_login)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener{
            val username : String = binding.emailUser.text.toString()
            val email : String = binding.emailRegister.text.toString()
            val password : String = binding.emailPassword.text.toString()
            viewModel.register(email,password) // i - 123456
        }
    }
}