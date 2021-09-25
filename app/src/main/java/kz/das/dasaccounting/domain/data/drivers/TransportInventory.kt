package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.core.extensions.getServerDateFromLong
import kz.das.dasaccounting.data.entities.driver.toEntity
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.OperationType

@Parcelize
data class TransportInventory(
    var comment: String,
    var dateTime: Long?,
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var model: String,
    var molUuid: String?,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var receiverName: String? = "",
    var uuid: String,
    var isPending: Boolean = false
) : OperationAct(), Parcelable

fun TransportInventory.toSent(): TransportSentInventory {
    return TransportSentInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        receiverName = this.receiverName,
        uuid = this.uuid,
        isPending = this.isPending
    )
}

fun TransportInventory.toAccepted(): TransportAcceptedInventory {
    return TransportAcceptedInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        receiverName = this.receiverName,
        uuid = this.uuid,
        isPending = this.isPending
    )
}

fun TransportAcceptedInventory.toDomain(): TransportInventory {
    return TransportInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        receiverName = this.receiverName,
        uuid = this.uuid,
        isPending = this.isPending
    )
}

fun TransportSentInventory.toDomain(): TransportInventory {
    return TransportInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        receiverName = this.receiverName,
        uuid = this.uuid,
        isPending = this.isPending
    )
}

fun TransportAcceptedInventory.toHistoryTransfer(): HistoryTransfer {
    return HistoryTransfer(
        title = this.model ?: "Транспорт",
        descr = "Гос. номер: " + this.stateNumber,
        date = this.dateTime ?: System.currentTimeMillis(),
        dateText = this.dateTime.getServerDateFromLong() ?: "Ошибка даты",
        quantity = "1",
        senderName = String.format("От кого: %s", this.senderName) ?: "",
        operationType = if (this.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status,
        qrData = DriverInventoryTypeConvertor().transportTransportToString(this.toDomain().toEntity()),
        isAwait = true,
        status = HistoryEnum.AWAIT.status
    )
}

fun TransportSentInventory.toHistoryTransfer(): HistoryTransfer {
    return HistoryTransfer(
        title = this.model ?: "Транспорт",
        descr = "Гос. номер: " + this.stateNumber,
        date = this.dateTime ?: System.currentTimeMillis(),
        dateText = this.dateTime.getServerDateFromLong() ?: "Ошибка даты",
        quantity = "1",
        senderName = String.format("Кому: %s", this.receiverName) ?: "",
        operationType = if (this.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status,
        isAwait = true,
        qrData = DriverInventoryTypeConvertor().transportTransportToString(this.toDomain().toEntity()),
        status = HistoryEnum.AWAIT.status
    )
}

@Parcelize
data class TransportSentInventory(
    var comment: String,
    var dateTime: Long?,
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var model: String,
    var molUuid: String?,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var receiverName: String? = "",
    var uuid: String,
    var isPending: Boolean = false
) : OperationAct(), Parcelable

@Parcelize
data class TransportAcceptedInventory(
    var comment: String,
    var dateTime: Long?,
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var model: String,
    var molUuid: String?,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var receiverName: String? = "",
    var uuid: String,
    var isPending: Boolean = false
) : OperationAct(), Parcelable
