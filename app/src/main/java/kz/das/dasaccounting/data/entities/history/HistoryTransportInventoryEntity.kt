package kz.das.dasaccounting.data.entities.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.core.extensions.getLongFromServerDate
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.HistoryTransportInventory
import kz.das.dasaccounting.domain.data.history.OperationType
import java.io.Serializable

@Entity(tableName = "history_transport_inventory")
data class HistoryTransportInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: String? = null,
    val uuid: String,
    val name: String? = null,
    val stateNumber: String? = null,
    val tsType: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val quantity: String? = null,
    val fullName: String? = null,
    val qrData: String? = null,
    val molUUID: String? = null,
    val status: String? = null
): Serializable

fun HistoryTransportInventoryEntity.toDomain(): HistoryTransportInventory {
    return HistoryTransportInventory(
        id = this.id,
        dateTime = this.dateTime,
        uuid = this.uuid,
        name = this.name,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        longitude = this.longitude,
        latitude = this.latitude,
        quantity = this.quantity,
        fullName = this.fullName,
        qrData = this.qrData,
        molUUID = this.molUUID,
        status = this.status
    )
}


fun HistoryTransportInventoryEntity.toHistoryTransfer(transferType: String? = null): HistoryTransfer {
    return HistoryTransfer(
        title = this.name ?: "Транспорт",
        descr = "Гос. номер: " + this.stateNumber,
        date = this.dateTime.getLongFromServerDate(),
        dateText = this.dateTime ?: "Ошибка даты",
        quantity = "1",
        senderName = String.format((if (this.status == "Принят") "От кого: %s" else "Кому: %s"), this.fullName) ?: "",
        operationType = if (this.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status,
        qrData = this.qrData,
        isAwait = false,
        status = this.status ?: "",
        transferType = transferType ?: "transport_type"
    )
}