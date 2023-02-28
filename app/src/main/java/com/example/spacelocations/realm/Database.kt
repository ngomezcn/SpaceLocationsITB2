package com.example.spacelocations.realm
/*
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoClientSettings.getDefaultCodecRegistry
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId

data class Item(
    var _id: ObjectId = ObjectId.get(),
    var complete: Boolean = false,
    var summary: String = "",
    var owner_id: String = "")

*/
object Database {

   // lateinit var config : SyncConfiguration

    suspend fun init()
    {
        /*val realmApp = App.create(
            AppConfiguration.Builder("application-0-uhlyj")
                .log(LogLevel.ALL)
                .build())

        realmApp.emailPasswordAuth.registerUser("naim.gomez.7e5@itb.cat", "p4ssword!")
        val creds = Credentials.emailPassword("naim.gomez.7e5@itb.cat", "p4ssword!")

        realmApp.login(creds)

        val userR = realmApp.currentUser!!

        config = SyncConfiguration.Builder(userR, setOf(Item::class))
            .initialSubscriptions { realm ->
                add(realm.query<Item>(), "All Items")
                //      add(
                //          realm.query<Item>("owner_id == $0", realmApp.currentUser!!.id),
                //          "User's Items"
                //      )
            }
            .waitForInitialRemoteData()
            .build()

        val realm = Realm.open(config)

        realm.subscriptions.waitForSynchronization()

        println("Realm Path: ${config.path}")*/
    }
}