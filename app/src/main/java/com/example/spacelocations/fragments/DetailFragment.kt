package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.spacelocations.R
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.databinding.FragmentAddMarkerBinding
import com.example.spacelocations.databinding.FragmentDetailBinding
import com.example.spacelocations.viewmodel.ViewModel


class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)

        val marker = viewModel.detailMarker.value!!

        binding.titleNameTextView.text = marker.title
        binding.dateTextView.text = marker.date
        binding.descriptionTextView.text = marker.description

        binding.latitudeTextView.text  = marker.position.latitude.toString()
        binding.longitudeTextView.text = marker.position.longitude.toString()

        activity?.let {
            Glide.with(it)
                .load(marker.photoUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.badgeImageView)
        }

        binding.deleteBtn.setOnClickListener {
            viewModel.deleteMarker(marker)
        }

        return binding.root
    }
}