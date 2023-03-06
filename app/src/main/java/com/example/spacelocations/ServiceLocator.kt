package com.example.spacelocations

import dev.mateuy.realmsample.daos.ItemsDao
import com.example.spacelocations.daos.RealmManager

/**
 * Singleton with services
 */
object ServiceLocator {
    val realmManager = RealmManager()
    lateinit var itemsDao: ItemsDao

    /**
     * Call when user logged in and realm created
     */
    fun configureRealm(){
        requireNotNull(realmManager.realm)
        val realm = realmManager.realm!!
        itemsDao = ItemsDao(realm, realmManager.realmApp.currentUser!!.id)
    }
}