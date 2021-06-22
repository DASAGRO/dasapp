package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity

@Dao
interface OfficeInventorySentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<OfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<OfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: OfficeInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: OfficeInventoryEntity)

    @get:Query("SELECT * FROM materials_sent")
    val allAsLiveData: LiveData<List<OfficeInventoryEntity>>

    @get:Query("SELECT * FROM materials_sent")
    val all: List<OfficeInventoryEntity>

    @Query("DELETE FROM materials_sent")
    fun removeAll()

    @Query("SELECT * FROM materials_sent WHERE materialUUID = :materialUUID")
    fun getItem(materialUUID: String): LiveData<OfficeInventoryEntity>

    @Delete
    fun removeItem(office: OfficeInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<OfficeInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}