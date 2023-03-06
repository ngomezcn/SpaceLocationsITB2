package com.example.spacelocations.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacelocations.Categories
import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*
class ViewModel : ViewModel() {
    var displayMarkerList = MutableLiveData<List<MarkerModel>>(listOf())
    private var rawMarkerList = MutableLiveData<MutableList<MarkerModel>>(mutableListOf())
    var filterCategory = MutableLiveData<Categories?>(null)
    var detailMarker = MutableLiveData<MarkerModel>()
    var selectedPosition = MutableLiveData<Position>()
    var mBinding = MutableLiveData<ActivityMainBinding>()
    val loggedIn = MutableLiveData<Boolean>(false)

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

    init {
        fetchData()
    }

    private fun fetchData()
    {
        CoroutineScope(Dispatchers.Main).launch {

        }
    }

    fun addMarker(markerModel: MarkerModel)
    {
        rawMarkerList.value!!.add(markerModel)
        categoryFilter()
    }

    fun register(email: String, password: String){
        CoroutineScope(Dispatchers.IO).launch{
            ServiceLocator.realmManager.register(email, password)
            loggedIn.postValue(true)
        }
    }
}