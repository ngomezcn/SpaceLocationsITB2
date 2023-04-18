package com.example.spacelocations.models.Position

import android.net.Uri
import com.example.spacelocations.Categories
import org.mongodb.kbson.ObjectId

data class MarkerModel(
    val id: io.realm.kotlin.types.ObjectId?,
    val position: Position,
    val title: String,
    val description: String,
    val date: String,
    val photoUri: Uri,
    val category: Categories
)

