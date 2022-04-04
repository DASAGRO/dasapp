package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.AcceptedTransportEntity

@Dao
interface DriverInventoryAcceptedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<AcceptedTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<AcceptedTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: AcceptedTransportEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: AcceptedTransportEntity)

    @Update(entity = AcceptedTransportEntity::class)
    fun updateEntity(item: AcceptedTransportEntity)

    @get:Query("SELECT * FROM accepted_transports")
    val allAsLiveData: LiveData<List<AcceptedTransportEntity>>

    @get:Query("SELECT * FROM accepted_transports")
    val all: List<AcceptedTransportEntity>

    @Query("DELETE FROM accepted_transports")
    fun removeAll()

    @Query("SELECT * FROM accepted_transports WHERE uuid = :uuid")
    fun getItem(uuid: String): LiveData<AcceptedTransportEntity>

    @Delete
    fun removeItem(warehouse: AcceptedTransportEntity)

    @Transaction
    fun reload(itemDaos: List<AcceptedTransportEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}