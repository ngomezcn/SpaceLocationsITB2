package com.example.spacelocations.realms

import com.example.spacelocations.models.Position.Position
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/*open class Item(
        @PrimaryKey
        var _id: ObjectId = ObjectId.create(),
        var complete: Boolean = false,
        var summary: String = "",
        var xd: String = "",
        var owner_id: String = ""
) : RealmObject {
        // Declaring empty contructor
        constructor() : this(owner_id = "") {}
}*/

open class Item(
        @PrimaryKey
        var _id: ObjectId = ObjectId.create(),
        var position: Position = Position(-0.1,-0.1),
        var title: String = "",
        var description: String = "",
        var date: String = "",
        var photoUri: String = "",
        var category: String = "",
        var owner_id: String = ""
) : RealmObject {
        // Declaring empty contructor
        constructor() : this(owner_id = "") {}
}
