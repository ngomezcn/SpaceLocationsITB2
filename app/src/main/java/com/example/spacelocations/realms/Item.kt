package com.example.spacelocations.realms

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Item(
        @PrimaryKey
        var _id: ObjectId = ObjectId.create(),
        var complete: Boolean = false,
        var summary: String = "",
        var owner_id: String = ""
) : RealmObject {
        // Declaring empty contructor
        constructor() : this(owner_id = "") {}
}