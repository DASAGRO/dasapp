package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.office.NomenclatureOfficeInventoryEntity

@Dao
interface NomenclaturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<NomenclatureOfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<NomenclatureOfficeInventoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NomenclatureOfficeInventoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: NomenclatureOfficeInventoryEntity)

    @get:Query("SELECT * FROM nomenclatures")
    val allAsLiveData: LiveData<List<NomenclatureOfficeInventoryEntity>>

    @get:Query("SELECT * FROM nomenclatures")
    val all: List<NomenclatureOfficeInventoryEntity>

    @Query("DELETE FROM nomenclatures")
    fun removeAll()

    @Query("SELECT * FROM nomenclatures WHERE fieldNumber = :fieldNumber")
    fun getItemAsLive(fieldNumber: String): LiveData<NomenclatureOfficeInventoryEntity>

    @Query("SELECT * FROM nomenclatures WHERE fieldNumber = :fieldNumber")
    fun getItem(fieldNumber: String): NomenclatureOfficeInventoryEntity?

    @Delete
    fun removeItem(nomenclatureOfficeInventoryEntity: NomenclatureOfficeInventoryEntity)

    @Transaction
    fun reloadItem(itemDao: NomenclatureOfficeInventoryEntity) {
        removeItem(itemDao)
        insert(itemDao)
    }

    @Transaction
    fun reload(itemDaos: List<NomenclatureOfficeInventoryEntity>) {
        removeAll()
        insertAll(itemDaos)
    }
    
}