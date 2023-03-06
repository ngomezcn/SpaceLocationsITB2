package com.example.spacelocations.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacelocations.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    val loggedIn = MutableLiveData<Boolean>()

    fun start(){
        if(ServiceLocator.realmManager.loggedIn()){
            CoroutineScope(Dispatchers.IO).launch {
                ServiceLocator.realmManager.configureRealm()
                loggedIn.postValue(true)
            }
        } else {
            loggedIn.postValue(false)
        }
    }
}