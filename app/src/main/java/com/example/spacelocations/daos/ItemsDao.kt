package dev.mateuy.realmsample.daos

import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.realms.Item
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Item Operations
 */
class ItemsDao(val realm: Realm, val userId: String){

   // fun listFlow() : Flow<List<wtf>> = realm.query<wtf>().find().asFlow().map { it.list.toList() }

    /*fun insertItem(text : String){
        realm.writeBlocking {
            val item = Itemm(summary = text, owner_id = userId)
            copyToRealm(item)
        }
    }*/
    fun listFlow() : Flow<List<Item>> = realm.query<Item>().find().asFlow().map { it.list.toList() }

    fun insertMarker(m : MarkerModel) {
       realm.writeBlocking {
            val marker = Item(
                longitude = m.position.longitude,
                latitude = m.position.latitude,
                title = m.title,
                description = m.description,
                date = m.date,
                category = m.category.toString(),
                photoUri = m.photoUri.toString() ,
                owner_id = userId)
            copyToRealm(marker)
        }
    }
}