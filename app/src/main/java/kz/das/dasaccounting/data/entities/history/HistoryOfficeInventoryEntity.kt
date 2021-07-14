package kz.das.dasaccounting.data.entities.history

import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.history.HistoryOfficeInventory

data class HistoryOfficeInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val dateTime: String? = null,
    val materialUUID: String? = null,
    val name: String? = null,
    val measurement: String? = null,
    val quantity: String? = null,
    val humidity: String? = null,
    val molUUID: String? = null,
    val fullName: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val status: String? = null
)

fun HistoryOfficeInventoryEntity.toDomain(): HistoryOfficeInventory {
    return HistoryOfficeInventory(
        id = this.id,
        dateTime = this.dateTime,
        materialUUID = this.materialUUID,
        name = this.name,
        measurement = this.measurement,
        quantity = this.quantity,
        humidity = this.humidity,
        molUUID = this.molUUID,
        fullName = this.fullName,
        longitude = this.longitude,
        latitude = this.latitude,
        status = this.status
    )
}