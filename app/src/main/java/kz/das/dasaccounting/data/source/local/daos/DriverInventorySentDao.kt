package kz.das.dasaccounting.data.source.local.daos


import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.AcceptedTransportEntity
import kz.das.dasaccounting.data.entities.driver.SentTransportEntity

@Dao
interface DriverInventorySentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<SentTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<SentTransportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: SentTransportEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: SentTransportEntity)

    @Update(entity = SentTransportEntity::class)
    fun updateEntity(item: SentTransportEntity)

    @get:Query("SELECT * FROM sent_transports")
    val allAsLiveData: LiveData<List<SentTransportEntity>>

    @get:Query("SELECT * FROM sent_transports")
    val all: List<SentTransportEntity>

    @Query("DELETE FROM sent_transports")
    fun removeAll()

    @Query("SELECT * FROM sent_transports WHERE uuid = :uuid")
    fun getItem(uuid: String): LiveData<SentTransportEntity>

    @Delete
    fun removeItem(item: SentTransportEntity)

    @Transaction
    fun reload(itemDaos: List<SentTransportEntity>) {
        removeAll()
        insertAll(itemDaos)
    }

}