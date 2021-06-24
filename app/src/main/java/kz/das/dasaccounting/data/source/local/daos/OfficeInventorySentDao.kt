package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.office.OfficeInventorySentEntity

@Dao
interface OfficeInventorySentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<OfficeInventorySentEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<OfficeInventorySentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: OfficeInventorySentEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: OfficeInventorySentEntity)

    @get:Query("SELECT * FROM materials_sent")
    val allAsLiveData: LiveData<List<OfficeInventorySentEntity>>

    @get:Query("SELECT * FROM materials_sent")
    val all: List<OfficeInventorySentEntity>

    @Query("DELETE FROM materials_sent")
    fun removeAll()

    @Query("SELECT * FROM materials_sent WHERE materialUUID = :materialUUID")
    fun getItem(materialUUID: String): LiveData<OfficeInventorySentEntity>

    @Delete
    fun removeItem(office: OfficeInventorySentEntity)

    @Transaction
    fun reload(itemDaos: List<OfficeInventorySentEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}