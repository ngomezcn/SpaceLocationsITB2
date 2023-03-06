package dev.mateuy.realmsample.daos

import com.example.spacelocations.realms.Item
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Item Operations
 */
class ItemsDao(val realm: Realm, val userId: String){

    fun listFlow() : Flow<List<Item>> = realm.query<Item>().sort("summary", Sort.DESCENDING).find().asFlow().map { it.list.toList() }

    fun insertItem(text: String){
        realm.writeBlocking {
            val item = Item(summary = text, owner_id = userId)
            copyToRealm(item)
        }
    }
}