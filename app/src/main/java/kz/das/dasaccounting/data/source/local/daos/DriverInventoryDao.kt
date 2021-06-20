package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.DriverInventoryEntity

@Dao
interface DriverInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<DriverInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<DriverInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DriverInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: DriverInventoryEntity)

    @get:Query("SELECT * FROM transports")
    val allAsLiveData: LiveData<List<DriverInventoryEntity>>

    @get:Query("SELECT * FROM transports")
    val all: List<DriverInventoryEntity>

    @Query("DELETE FROM transports")
    fun removeAll()

    @Query("SELECT * FROM transports WHERE tcUUID = :tcUUID")
    fun getItem(tcUUID: String): LiveData<DriverInventoryEntity>

    @Delete
    fun removeItem(warehouse: DriverInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<DriverInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }
    
}