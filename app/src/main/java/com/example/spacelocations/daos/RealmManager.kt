package com.example.spacelocations.daos

import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.realms.Item
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration

/**
 * Manages the realm app
 */
class RealmManager{
    val realmApp = App.create(AppConfiguration.Builder("application-0-ibiow").log(LogLevel.ALL).build())
    var realm : Realm? = null

    fun loggedIn() = realmApp.currentUser?.loggedIn ?: false

    suspend fun register(username: String, password: String) {
        realmApp.emailPasswordAuth.registerUser(username, password)
        login(username, password)
    }
    suspend fun login(username: String, password: String) {
        val creds = Credentials.emailPassword(username, password)
        realmApp.login(creds)
        configureRealm()
    }

    suspend fun configureRealm(){
        requireNotNull(realmApp.currentUser)

        val user = realmApp.currentUser!!
        val config = SyncConfiguration.Builder(user, setOf(Item::class))
            .initialSubscriptions { realm ->
                add(
                    realm.query<Item>(),
                    "All Items"
                )
            }
            .waitForInitialRemoteData()
            .build()
        realm = Realm.open(config)
        realm!!.subscriptions.waitForSynchronization()


        ServiceLocator.configureRealm()
    }
}