package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.R
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.databinding.FragmentMapBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position
import com.example.spacelocations.viewmodel.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    //lateinit var map: GoogleMap            findNavController().navigate(R.id.map_to_addmarker)

    private val viewModel: ViewModel by activityViewModels()
    lateinit var map: GoogleMap

    lateinit var binding: FragmentMapBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        createMap()

        binding.addMarkerButton.setOnClickListener {
            if(viewModel.mBinding.value != null)
            {
                viewModel.mBinding.value!!.bottomNavigation.visibility = View.GONE;
                binding.reyclerButton.visibility = View.GONE;
                binding.addMarkerButton.visibility = View.GONE;
                binding.nextAddMarker.visibility = View.VISIBLE;
                binding.crossAim1.visibility = View.VISIBLE;
                binding.crossAim2.visibility = View.VISIBLE;

                binding.nextAddMarker.setOnClickListener {
                    viewModel.selectedPosition.postValue(Position(latitude = map.cameraPosition.target.latitude, longitude = map.cameraPosition.target.longitude))
                    findNavController().navigate(R.id.map_to_addmarker)
                }
            }
        }

        binding.reyclerButton.setOnClickListener {
            findNavController().navigate(R.id.map_to_recycler)
        }

        viewModel.filterCategory.observe(viewLifecycleOwner, Observer {
            viewModel.categoryFilter()
        })

        viewModel.displayMarkerList.observe(viewLifecycleOwner, Observer {
            if (this::map.isInitialized) {
                map.clear()
                loadMarkers()
            }
        })

        return binding.root
    }

    private fun createMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        loadMarkers()
    }
    /*private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }*/

    private fun loadMarkers()
    {
        if(viewModel.displayMarkerList.value != null)
        {
            for(m in viewModel.displayMarkerList.value!!.iterator()){
                createMarker(m)
            }
        }
    }

    private fun createMarker(marker: MarkerModel){
        val coordinates = LatLng(marker.position.latitude, marker.position.longitude)
        val myMarker = MarkerOptions().position(coordinates).title(marker.title)
        map.addMarker(myMarker)
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 5000, null)
    }
}