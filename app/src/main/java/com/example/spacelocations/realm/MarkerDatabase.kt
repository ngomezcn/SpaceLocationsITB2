package com.example.spacelocations.realm
/*
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


/*
open class MarkerTable(
    @PrimaryKey
    var _id: ObjectId = ObjectId.create(),
    var owner_id: String = "",
    var title : String = "",
    var description: String = "",
    var date : String = "",
    var photoUri  : String = "",
    var category  : String = "",
    var latitude  : Double = 0.0,
    var longitude : Double = 0.0,

    ) : RealmObject {
    // Declaring empty contructor
    constructor() : this(owner_id = "") {}
    //var doAfter: RealmList<Item>? = realmListOf()
    override fun toString() = "Item($_id, $title)"
}
*/