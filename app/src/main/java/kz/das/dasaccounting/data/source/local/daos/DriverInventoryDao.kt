package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.InventoryTransportEntity

@Dao
interface DriverInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<InventoryTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<InventoryTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: InventoryTransportEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: InventoryTransportEntity)

    @get:Query("SELECT * FROM transports")
    val allAsLiveData: LiveData<List<InventoryTransportEntity>>

    @get:Query("SELECT * FROM transports")
    val all: List<InventoryTransportEntity>

    @Query("DELETE FROM transports")
    fun removeAll()

    @Query("SELECT * FROM transports WHERE uuid = :uuid")
    fun getItem(uuid: String): LiveData<InventoryTransportEntity>

    @Delete
    fun removeItem(warehouse: InventoryTransportEntity)

    @Transaction
    fun reload(itemDaos: List<InventoryTransportEntity>) {
        removeAll()
        insertAll(itemDaos)
    }
    
}