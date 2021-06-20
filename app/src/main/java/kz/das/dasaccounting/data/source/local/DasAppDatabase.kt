package kz.das.dasaccounting.data.source.local

import android.content.Context
import androidx.room.*
import kz.das.dasaccounting.data.entities.driver.DriverInventoryEntity
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity
import kz.das.dasaccounting.data.entities.warehouse.WarehouseInventoryEntity
import kz.das.dasaccounting.data.source.local.daos.DriverInventoryDao
import kz.das.dasaccounting.data.source.local.daos.OfficeInventoryDao
import kz.das.dasaccounting.data.source.local.daos.WarehouseInventoryDao

@Database(version = 1, exportSchema = false, entities = [OfficeInventoryEntity::class, WarehouseInventoryEntity::class, DriverInventoryEntity::class])
abstract class DasAppDatabase: RoomDatabase() {

    abstract fun officeInventoryDao(): OfficeInventoryDao

    abstract fun warehouseInventoryDao(): WarehouseInventoryDao

    abstract fun driverInventoryDao(): DriverInventoryDao

    companion object {
        private val LOCK = Any()
        @Volatile
        private var sInstance: DasAppDatabase? = null
        private const val DATABASE_NAME = "das_app_db"

        fun getInstance(context: Context): DasAppDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(
                            context.applicationContext, DasAppDatabase::class.java, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return sInstance
        }

        fun resetDb() { sInstance = null }

    }

}