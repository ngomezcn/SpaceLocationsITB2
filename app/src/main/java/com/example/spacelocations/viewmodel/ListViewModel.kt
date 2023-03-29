package com.example.spacelocations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.spacelocations.ServiceLocator

class ListViewModel : ViewModel() {
    val itemsDao = ServiceLocator.itemsDao
    val items = itemsDao.listFlow().asLiveData()
}