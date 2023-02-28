package com.example.spacelocations.DAO

import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.realm.Database

class MarkerDAO {

    fun add(marker: MarkerModel)
    {
        /*val realm = Realm.open(Database.config)
        realm.writeBlocking {
            val markerTable = markerToTable(marker)

            copyToRealm(markerTable)
        }*/
    }
}