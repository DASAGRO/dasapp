package kz.das.dasaccounting.data.entities.warehouse

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import java.io.Serializable

@Entity(tableName = "warehouses")
data class WarehouseInventoryEntity(
    val id: Int = 0,
    val date: Long = 0,
    var name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var senderUUID: String? = null,
    val sealNumber: String? = null,
    @PrimaryKey
    val storeUUID: String,
    var requestId: String?,
    val type: String? = null
) : Serializable


fun WarehouseInventoryEntity.toDomain(): WarehouseInventory {
    return WarehouseInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        senderUUID = this.senderUUID,
        sealNumber = this.sealNumber,
        requestId = this.requestId,
        storeUUID = this.storeUUID,
        type = this.type
    )
}

fun WarehouseInventory.toEntity(): WarehouseInventoryEntity {
    return WarehouseInventoryEntity(
        id = this.id,
        date = this.date,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        senderUUID = this.senderUUID,
        sealNumber = this.sealNumber,
        requestId = this.requestId,
        storeUUID = this.storeUUID,
        type = this.type
    )
}