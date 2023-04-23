package com.example.spacelocations.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spacelocations.OnClickListener
import com.example.spacelocations.adapter.RecyclerAdapter
import com.example.spacelocations.databinding.FragmentRecyclerBinding
import com.example.spacelocations.MarkerR
import com.example.spacelocations.viewmodel.ViewModel

class RecyclerFragment : Fragment(), OnClickListener {

    lateinit var binding: FragmentRecyclerBinding
    private lateinit var launchAdapter: RecyclerAdapter
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentRecyclerBinding.inflate(layoutInflater)

        viewModel.filterCategory.observe(viewLifecycleOwner, Observer {
            viewModel.categoryFilter()
        })

        viewModel.displayMarkerList.observe(viewLifecycleOwner, Observer {
            setUpRecyclerView()
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        launchAdapter = RecyclerAdapter(viewModel.displayMarkerList.value!!, this)
        binding.recyclerView.apply {
            setHasFixedSize(true) //Optimitza el rendiment de l’app
            layoutManager = GridLayoutManager(context, 1)
            adapter = launchAdapter
        }
    }

    override fun onResume() {
        setUpRecyclerView()
        super.onResume()
    }

    override fun onClick(marker: MarkerR) {
        viewModel.detailMarker.postValue(marker)

        findNavController().navigate(com.example.spacelocations.R.id.recycler_to_detail)
    }
}