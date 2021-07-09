package kz.das.dasaccounting.data.entities.warehouse

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "warehouses")
data class WarehouseInventoryEntity(
    val id: Int = 0,
    val date: Long = 0,
    var name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val senderUUID: String? = null,
    val sealNumber: String? = null,
    @PrimaryKey
    val storeUUID: String? = null,
    val type: String? = null
): Serializable