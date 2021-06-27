package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.TransportInventoryEntity

@Dao
interface DriverInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<TransportInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<TransportInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: TransportInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: TransportInventoryEntity)

    @get:Query("SELECT * FROM transports")
    val allAsLiveData: LiveData<List<TransportInventoryEntity>>

    @get:Query("SELECT * FROM transports")
    val all: List<TransportInventoryEntity>

    @Query("DELETE FROM transports")
    fun removeAll()

    @Query("SELECT * FROM transports WHERE uuid = :uuid")
    fun getItemAsLiveData(uuid: String): LiveData<TransportInventoryEntity>

    @Query("SELECT * FROM transports WHERE uuid = :uuid")
    fun getItem(uuid: String): TransportInventoryEntity

    @Delete
    fun removeItem(warehouse: TransportInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<TransportInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }
    
}