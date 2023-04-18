package com.example.spacelocations.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.Categories
import com.example.spacelocations.R
import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.databinding.FragmentMapBinding
import com.example.spacelocations.realms.Item
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position
import com.example.spacelocations.viewmodel.ListViewModel
import com.example.spacelocations.viewmodel.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: ViewModel by activityViewModels()
    lateinit var map: GoogleMap
    lateinit var mapFragment : SupportMapFragment
    lateinit var binding: FragmentMapBinding
    private lateinit var listViewModel: ListViewModel

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        createMap()

        binding.addMarkerButton.setOnClickListener {
            if(viewModel.mBinding.value != null)
            {
                binding.reyclerButton.visibility = View.GONE;
                binding.addMarkerButton.visibility = View.GONE;
                binding.nextAddMarker.visibility = View.VISIBLE;
                binding.crossAim1.visibility = View.VISIBLE;
                binding.crossAim2.visibility = View.VISIBLE;
                binding.myLocationBtnTest.visibility = View.VISIBLE

                binding.myLocationBtnTest.visibility = View.VISIBLE;

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

        binding.myLocationBtnTest.setOnClickListener {
            val latLng = LatLng( 40.416751 ,-3.703832)
            val zoom : Float = 15.0f
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        }

        return binding.root
    }

    private fun createMap(){
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment?.getMapAsync(this)
    }

    private fun allPermissionsGranted() = MapFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun loadMarkersFromRealm(list : List<Item>)
    {
        for(i in list)
        {
            var abb = Categories.All
            when(i.category)
            {
                Categories.All.toString() -> abb = Categories.All
                Categories.LifOff.toString() -> abb = Categories.LifOff
                Categories.PrimaryStage.toString() -> abb = Categories.PrimaryStage
                Categories.SecondaryStage.toString() -> abb = Categories.SecondaryStage
            }

            val m = MarkerModel(i._id, Position(i.latitude, i.longitude), i.title, i.description, i.date, Uri.parse(i.photoUri), abb)

            viewModel.rawMarkerList.value!!.add(m)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                MapFragment.REQUIRED_PERMISSIONS,
                MapFragment.REQUEST_CODE_PERMISSIONS
            )
            return
        }

        map = googleMap

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.items.observe(viewLifecycleOwner){list ->

            loadMarkersFromRealm(list)
            viewModel.categoryFilter()
            loadMarkers()
            //rawMarkerList.value!!.add(markerModel)
        }
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