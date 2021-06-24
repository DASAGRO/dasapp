package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.office.OfficeInventoryAcceptedEntity

@Dao
interface OfficeInventoryAcceptedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<OfficeInventoryAcceptedEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<OfficeInventoryAcceptedEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: OfficeInventoryAcceptedEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: OfficeInventoryAcceptedEntity)

    @get:Query("SELECT * FROM materials_accepted")
    val allAsLiveData: LiveData<List<OfficeInventoryAcceptedEntity>>

    @get:Query("SELECT * FROM materials_accepted")
    val all: List<OfficeInventoryAcceptedEntity>

    @Query("DELETE FROM materials_accepted")
    fun removeAll()

    @Query("SELECT * FROM materials_accepted WHERE materialUUID = :materialUUID")
    fun getItem(materialUUID: String): LiveData<OfficeInventoryAcceptedEntity>

    @Delete
    fun removeItem(officeInventory: OfficeInventoryAcceptedEntity)

    @Transaction
    fun reload(itemDaos: List<OfficeInventoryAcceptedEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}