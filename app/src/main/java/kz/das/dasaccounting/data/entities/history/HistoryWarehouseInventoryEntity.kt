package kz.das.dasaccounting.data.entities.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.history.HistoryWarehouseInventory

@Entity(tableName = "history_warehouse_inventory")
class HistoryWarehouseInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val dateTime: String? = null,
    val uuid: String? = null,
    val name: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val sealNumber: String? = null,
    val fullName: String? = null,
    val molUUID: String? = null,
    val status: String? = null
)

fun HistoryWarehouseInventoryEntity.toDomain(): HistoryWarehouseInventory {
    return HistoryWarehouseInventory(
        id = this.id,
        dateTime = this.dateTime,
        uuid = this.uuid,
        name = this.name,
        longitude = this.longitude,
        latitude = this.latitude,
        sealNumber = this.sealNumber,
        fullName = this.fullName,
        molUUID = this.molUUID,
        status = this.status
    )
}