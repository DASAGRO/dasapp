package kz.das.dasaccounting.data.entities.office

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.core.extensions.getServerDateFromLong
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryAcceptedTypeConvertor
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.OperationType
import kz.das.dasaccounting.domain.data.office.OfficeAcceptedInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.io.Serializable

@Entity(tableName = "materials_accepted")
data class OfficeInventoryAcceptedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0,
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String,
    val senderUUID: String? = null,
    val receiverUUID: String? = null,
    val receiverName: String? = null,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    val quantity: Double? = null,
    val type: String? = null,
    val acceptedAt: Long? = 0,
    val sendAt: Long? = 0,
    val syncRequire: Int = 0,
    val senderName: String? = null,
    val isSend: Int = 0,
    val isAccepted: Int = 0,
    var comment: String
) : Serializable


fun OfficeInventoryAcceptedEntity.toDomain(): OfficeInventory {
    return OfficeInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        receiverUUID = this.receiverUUID,
        receiverName = this.receiverName,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire
    )
}


fun OfficeInventoryAcceptedEntity.toAccepted(): OfficeAcceptedInventory {
    return OfficeAcceptedInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        receiverUUID = this.receiverUUID,
        receiverName = this.receiverName,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire
    )
}

fun OfficeInventoryAcceptedEntity.toHistory(): HistoryTransfer {
    return HistoryTransfer(
        title = this.name ?: "Продукт",
        descr = ("Количество:" +
                " " + this.quantity +
                " " + this.type + "\n" +
                String.format("От кого: %s", this.senderName)),
        date = this.date ?: 0L,
        dateText = this.date.getServerDateFromLong() ?: "Ошибка даты",
        quantity = this.quantity.toString(),
        senderName = String.format("От кого: %s", this.senderName) ?: "",
        operationType = OperationType.OFFICE.status,
        isAwait = false,
        qrData = OfficeInventoryAcceptedTypeConvertor().officeAcceptedInventoryToString(this),
        status = HistoryEnum.AWAIT.status
    )
}


fun OfficeInventory.toAcceptedEntity(): OfficeInventoryAcceptedEntity {
    return OfficeInventoryAcceptedEntity(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        comment = ""
    )
}