package com.example.spacelocations.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacelocations.Categories
import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.ServiceLocator.itemsDao
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.models.Position.Position
import com.example.spacelocations.MarkerR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {
    var displayMarkerList = MutableLiveData<List<MarkerR>>(listOf())
    var rawMarkerList = MutableLiveData<MutableList<MarkerR>>(mutableListOf())

    var filterCategory = MutableLiveData<Categories?>(null)
    var detailMarker = MutableLiveData<MarkerR>()
    var selectedPosition = MutableLiveData<Position>()
    var mBinding = MutableLiveData<ActivityMainBinding>()
    val loggedIn = MutableLiveData<Boolean>(false)
    val user = MutableLiveData<Boolean>(false)

    val editMode = MutableLiveData<Boolean>(false)

    fun categoryFilter()
    {
        var result = mutableListOf<MarkerR>()

        if(filterCategory.value == null)
        {
            result = rawMarkerList.value!!
        } else
        {
            for(i in rawMarkerList.value!!.iterator())
            {
                if(i.category == filterCategory.value.toString())
                {
                    result.add(i)
                }
            }
        }

        displayMarkerList.postValue(result)
    }



    fun logout()
    {
        CoroutineScope(Dispatchers.IO).launch{
            filterCategory.postValue(null)
            clear()
            ServiceLocator.realmManager.logout()
            loggedIn.postValue(false)
        }
    }

    fun deleteMarker(marker: MarkerR)
    {
        marker._id?.let { itemsDao.deleteItem(it) }
    }

    fun clear()
    {
        displayMarkerList.postValue(listOf())
        rawMarkerList.postValue(mutableListOf())
    }
}

