package com.example.spacelocations.models.Position

import android.net.Uri
import com.example.spacelocations.Categories
import java.time.LocalDateTime

data class MarkerModel(
    val position: Position,
    val title: String,
    val description: String,
    val date: String,
    val photoUri: Uri,
    val category: Categories
)