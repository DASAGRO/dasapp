package kz.das.dasaccounting.data.entities.warehouse

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "warehouses")
data class WarehouseInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val receiverUUID: String? = null,
    val sealNumber: String? = null,
    val storeUUID: String? = null,
    val type: String? = null
): Serializable