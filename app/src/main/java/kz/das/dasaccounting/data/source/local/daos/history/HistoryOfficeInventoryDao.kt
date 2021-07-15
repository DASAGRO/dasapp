package kz.das.dasaccounting.data.source.local.daos.history

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.history.HistoryOfficeInventoryEntity

@Dao
interface HistoryOfficeInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<HistoryOfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<HistoryOfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryOfficeInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: HistoryOfficeInventoryEntity)

    @get:Query("SELECT * FROM history_office_inventory")
    val allAsLiveData: LiveData<List<HistoryOfficeInventoryEntity>>

    @get:Query("SELECT * FROM history_office_inventory")
    val all: List<HistoryOfficeInventoryEntity>

    @Query("DELETE FROM history_office_inventory")
    fun removeAll()

    @Query("SELECT * FROM history_office_inventory WHERE materialUUID = :uuid")
    fun getItem(uuid: String): LiveData<HistoryOfficeInventoryEntity>

    @Delete
    fun removeItem(item: HistoryOfficeInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<HistoryOfficeInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

    
}