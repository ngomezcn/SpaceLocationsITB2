package dev.mateuy.realmsample.daos

import com.example.spacelocations.MarkerR
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Item Operations
 */
class ItemsDao(val realm: Realm, val userId: String){

    fun listFlow() : Flow<List<MarkerR>> = realm.query<MarkerR>().find().asFlow().map { it.list.toList() }

    fun insertItem(title: String, description: String, date: String, lat: Double, lon: Double, category: String, image: ByteArray?){
        val item = MarkerR(title = title, description = description, date = date, latitude = lat, longitude = lon, image = image, category = category, owner_id = userId)

        realm.writeBlocking {
            println("$title, $lat, $lon")
            copyToRealm(item)

        }
        /*Thread.sleep(1_500)

        realm.writeBlocking {
            val frog: MarkerR? = this.query<MarkerR>("_id == $0", item._id!!).first().find()
            frog?.title = "update"
        }*/
    }

    fun updateItem(id : ObjectId, title: String, description: String, category: String, image: ByteArray?){

        realm.writeBlocking {
            val frog: MarkerR? = this.query<MarkerR>("_id == $0", id).first().find()
            frog?.title = title
            frog?.description = description
            frog?.category = category
            frog?.image = image
        }
    }

    fun deleteItem(id: io.realm.kotlin.types.ObjectId){
       realm.writeBlocking {
            delete(query<MarkerR>("_id = $0", id).find())
        }
    }
}