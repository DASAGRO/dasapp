package kz.das.dasaccounting.data.entities.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.extensions.getLongFromServerDate
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.HistoryWarehouseInventory
import kz.das.dasaccounting.domain.data.history.OperationType
import java.io.Serializable

@Entity(tableName = "history_warehouse_inventory")
class HistoryWarehouseInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: String? = null,
    val uuid: String,
    val name: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val sealNumber: String? = null,
    val fullName: String? = null,
    val molUUID: String? = null,
    val status: String? = null
): Serializable

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

fun HistoryWarehouseInventoryEntity.toHistoryTransfer(): HistoryTransfer {
    return HistoryTransfer(
        title = this.name ?: "Склад",
        descr = ("Номер пломбы: " + " " + this.sealNumber),
        date = this.dateTime.getLongFromServerDate(),
        dateText = this.dateTime ?: "Ошибка даты",
        quantity = 0.0,
        senderName = String.format("От кого: %s", this.fullName) ?: "",
        operationType = OperationType.WAREHOUSE.status,
        isAwait = false,
        status = this.status ?: ""
    )
}