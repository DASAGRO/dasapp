package kz.das.dasaccounting.data.source.local.daos.history

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.history.HistoryWarehouseInventoryEntity

@Dao
interface HistoryWarehouseInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<HistoryWarehouseInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<HistoryWarehouseInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryWarehouseInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: HistoryWarehouseInventoryEntity)

    @get:Query("SELECT * FROM history_warehouse_inventory")
    val allAsLiveData: LiveData<List<HistoryWarehouseInventoryEntity>>

    @get:Query("SELECT * FROM history_warehouse_inventory")
    val all: List<HistoryWarehouseInventoryEntity>

    @Query("DELETE FROM history_warehouse_inventory")
    fun removeAll()

    @Query("SELECT * FROM history_warehouse_inventory WHERE uuid = :uuid")
    fun getItem(uuid: String): LiveData<HistoryWarehouseInventoryEntity>

    @Delete
    fun removeItem(item: HistoryWarehouseInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<HistoryWarehouseInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }


}