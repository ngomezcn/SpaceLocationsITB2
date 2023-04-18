package dev.mateuy.realmsample.daos

import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.realms.Item
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId


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

    fun deleteItemById(itemId: io.realm.kotlin.types.ObjectId) {
        realm.writeBlocking {
            val itemToDelete = realm.query<Item>().query("_id = $0", itemId).first()

            val liveItemToDelete = realm.findLatest(frozenItemToDelete)

            // Eliminar el objeto en vivo
            liveItemToDelete?.deleteFromRealm()
            delete(itemToDelete)
            //itemToDelete?.deleteFromRealm()
        }
    }

    fun delete(m : MarkerModel)
    {
        //val items = realm.query<Item>().find().toList()

        CoroutineScope(Dispatchers.Default).launch {
            m.id?.let { deleteItemById(it) }


           // val result: RealmResults<Item> =
                //realm.where(Item::class.java).equalTo("id", 5).findAll()
            //result.deleteAllFromRealm()
            //realm.write {
            //    val itemToDelete = realm.query<Item>().query("_id = $0", m.id)
             //   delete(itemToDelete)
            //}


        }

    }
}