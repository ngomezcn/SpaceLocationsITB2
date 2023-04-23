package com.example.spacelocations.models.m

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Positions(
    @PrimaryKey
    var _id: ObjectId = ObjectId.create(),
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var category: String = "",
    var image: ByteArray? = null,
    var owner_id: String = ""
) : RealmObject {
    constructor() : this(owner_id = "") {}
    override fun toString() = "Posicions($title, Cords($lat, $lon))"
}