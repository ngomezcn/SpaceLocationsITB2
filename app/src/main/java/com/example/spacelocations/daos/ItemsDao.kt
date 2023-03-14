package dev.mateuy.realmsample.daos

import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.realms.Item
import com.google.android.gms.maps.model.Marker
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Item Operations
 */
class ItemsDao(val realm: Realm, val userId: String){

    //fun listFlow() : Flow<List<Item>> = realm.query<Item>().sort("summary", Sort.DESCENDING).find().asFlow().map { it.list.toList() }

    /*fun insertItem(text : String){
        realm.writeBlocking {
            val item = Itemm(summary = text, owner_id = userId)
            copyToRealm(item)
        }
    }*/

    fun insertMarker(m : MarkerModel){
        realm.writeBlocking {
            //val item = Item(summary = text, owner_id = userId)

            val marker = Item(
                position = m.position,
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