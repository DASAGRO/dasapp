package kz.das.dasaccounting.data.source.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.FligelProductEntity

@Dao
interface FligelDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemDaos: List<FligelProductEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllWithIgnore(itemDaos: List<FligelProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FligelProductEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWithIgnore(item: FligelProductEntity)

    @get:Query("SELECT * FROM fligel_products")
    val allAsLiveData: LiveData<List<FligelProductEntity>>

    @get:Query("SELECT * FROM fligel_products")
    val all: List<FligelProductEntity>

    @Query("DELETE FROM fligel_products")
    fun removeAll()

    @Query("SELECT * FROM fligel_products WHERE id = :id")
    fun getItem(id: Int): LiveData<FligelProductEntity>

    @Delete
    fun removeItem(fligelData: FligelProductEntity)

    @Transaction
    fun reload(itemDaos: List<FligelProductEntity>) {
        removeAll()
        insertAll(itemDaos)
    }
}
