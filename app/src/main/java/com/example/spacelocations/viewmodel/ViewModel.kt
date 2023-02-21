package com.example.spacelocations.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacelocations.Categories
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position

class ViewModel : ViewModel() {
    var displayMarkerList = MutableLiveData<List<MarkerModel>>(listOf())
    private var rawMarkerList = MutableLiveData<MutableList<MarkerModel>>(mutableListOf())
    var filterCategory = MutableLiveData<Categories?>(null)

    var detailMarker = MutableLiveData<MarkerModel>()

    var selectedPosition = MutableLiveData<Position>()

    var mBinding = MutableLiveData<ActivityMainBinding>()

    fun categoryFilter()
    {
        var result = mutableListOf<MarkerModel>()

        if(filterCategory.value == null)
        {
            result = rawMarkerList.value!!
        } else
        {
            for(i in rawMarkerList.value!!.iterator())
            {
                if(i.category == filterCategory.value)
                {
                    result.add(i)
                }
            }
        }

        displayMarkerList.postValue(result)
    }

    fun addMarker(markerModel: MarkerModel)
    {
        rawMarkerList.value!!.add(markerModel)
        categoryFilter()
    }
}