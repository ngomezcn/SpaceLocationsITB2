package com.example.spacelocations.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.spacelocations.R
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.databinding.FragmentAddMarkerBinding
import com.example.spacelocations.databinding.FragmentDetailBinding
import com.example.spacelocations.viewmodel.ViewModel
import kotlinx.coroutines.delay


class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        viewModel.editMode.postValue(false)

        val marker = viewModel.detailMarker.value!!

        binding.titleNameTextView.text = marker.title
        binding.dateTextView.text = marker.date
        binding.descriptionTextView.text = marker.description

        binding.latitudeTextView.text  = marker.latitude.toString()
        binding.longitudeTextView.text = marker.longitude.toString()

        val bitmap = BitmapFactory.decodeByteArray(marker.image, 0, marker.image!!.size)
        binding.badgeImageView.setImageBitmap(bitmap)

        /*activity?.let {
            Glide.with(it)
                .load(marker.photoUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.badgeImageView)
        }*/

        binding.deleteBtn.setOnClickListener {
            viewModel.deleteMarker(marker)
            Thread.sleep(500)
            findNavController().navigate(R.id.detail_to_map)
        }

        binding.editBtn.setOnClickListener {
            Thread.sleep(500)

            viewModel.editMode.postValue(true)

            findNavController().navigate(R.id.detail_to_addmarker)
        }

        return binding.root
    }
}