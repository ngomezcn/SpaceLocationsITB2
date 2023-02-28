package com.example.spacelocations.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacelocations.Categories
import com.example.spacelocations.databinding.ActivityMainBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position
import com.example.spacelocations.realm.Database
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

open class ItemRealm(
    @PrimaryKey
    var _id: ObjectId = ObjectId.create(),
    var complete: Boolean = false,
    var summary: String = "",
    var owner_id: String = ""
) : RealmObject {
    // Declaring empty contructor
    constructor() : this(owner_id = "") {}
    //var doAfter: RealmList<Item>? = realmListOf()
    override fun toString() = "Item($_id, $summary)"
}

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

    init {
        fetchData()
    }

    private fun fetchData()
    {
        CoroutineScope(Dispatchers.Main).launch {
            //Database.init()

            val config = RealmConfiguration.Builder(setOf(ItemRealm::class))
                .deleteRealmIfMigrationNeeded()
                // .directory("customPath")
                .build()
            println("Realm Path: ${config.path}")
            val realm = Realm.open(config)

            realm.writeBlocking {
                val itemRealm = ItemRealm(summary = "Some summary ${Date()}"
                    // doAfter = query<Item>().find().take(2).toRealmList()
                )
                copyToRealm(itemRealm)
            }

            val items = realm.query<ItemRealm>().find()
            items.forEach { println(it.summary) }
        }
    }

    fun addMarker(markerModel: MarkerModel)
    {
        rawMarkerList.value!!.add(markerModel)
        categoryFilter()
    }
}