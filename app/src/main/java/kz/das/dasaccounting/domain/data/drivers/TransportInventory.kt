package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.core.extensions.getServerDateFromLong
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
    var storeUUID: String? = null,
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
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
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
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid,
        isPending = this.isPending
    )
}

fun TransportInventory.toHistoryTransfer(): HistoryTransfer {
    return HistoryTransfer(
        title = this.model ?: "Транспорт",
        descr = "Гос. номер: " + this.stateNumber,
        date = this.dateTime ?: System.currentTimeMillis(),
        dateText = this.dateTime.getServerDateFromLong() ?: "Ошибка даты",
        quantity = "1",
        senderName = String.format("От кого: %s", this.senderName) ?: "",
        operationType = if (this.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status,
        isAwait = true,
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
    var storeUUID: String? = null,
    var senderUUID: String? = null,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
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
    var storeUUID: String? = null,
    var senderUUID: String? = null,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var uuid: String,
    var isPending: Boolean = false
) : OperationAct(), Parcelable
