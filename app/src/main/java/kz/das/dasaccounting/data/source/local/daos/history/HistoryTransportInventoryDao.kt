package kz.das.dasaccounting.data.source.local.daos.history

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.history.HistoryTransportInventoryEntity

@Dao
interface HistoryTransportInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<HistoryTransportInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<HistoryTransportInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryTransportInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: HistoryTransportInventoryEntity)

    @get:Query("SELECT * FROM history_transport_inventory")
    val allAsLiveData: LiveData<List<HistoryTransportInventoryEntity>>

    @get:Query("SELECT * FROM history_transport_inventory")
    val all: List<HistoryTransportInventoryEntity>

    @Query("DELETE FROM history_transport_inventory")
    fun removeAll()

    @Query("SELECT * FROM history_transport_inventory WHERE uuid = :uuid")
    fun getItem(uuid: String): LiveData<HistoryTransportInventoryEntity>

    @Delete
    fun removeItem(item: HistoryTransportInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<HistoryTransportInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }


}