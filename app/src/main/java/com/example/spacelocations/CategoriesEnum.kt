package com.example.spacelocations

enum class Categories(private val category: String) {
    All( "All"),
    LifOff( "Lift Off"),
    PrimaryStage("Primary Stage"),
    SecondaryStage("Secondary Stage");
    override fun toString(): String {
        return category
    }
}