package com.example.spacelocations

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class MarkerR(
        @PrimaryKey
        var _id: ObjectId = ObjectId.create(),
        var latitude: Double = -0.1,
        var longitude: Double = -0.1,
        var title: String = "",
        var description: String = "",
        var date: String = "",
        //var photoUri: String = "",
        var image: ByteArray? = null,
        var category: String = "",
        var owner_id: String = ""
) : RealmObject {
        // Declaring empty contructor
        constructor() : this(owner_id = "") {}
}
