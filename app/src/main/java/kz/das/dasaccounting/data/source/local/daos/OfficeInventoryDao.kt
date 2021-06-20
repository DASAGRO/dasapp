package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity

@Dao
interface OfficeInventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<OfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<OfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: OfficeInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: OfficeInventoryEntity)

    @get:Query("SELECT * FROM materials")
    val allAsLiveData: LiveData<List<OfficeInventoryEntity>>

    @get:Query("SELECT * FROM materials")
    val all: List<OfficeInventoryEntity>

    @Query("DELETE FROM materials")
    fun removeAll()

    @Query("SELECT * FROM materials WHERE materialUUID = :materialUUID")
    fun getItem(materialUUID: String): LiveData<OfficeInventoryEntity>

    @Delete
    fun removeItem(warehouse: OfficeInventoryEntity)

    @Transaction
    fun reload(itemDaos: List<OfficeInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}