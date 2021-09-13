package kz.das.dasaccounting.domain.data.office

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.core.extensions.getServerDateFromLong
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.OperationType

@Parcelize
data class OfficeInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var requestId: String? = null,
    var storeUUID: String? = null,
    var quantity: Double? = null,
    var type: String? = null,
    var syncRequire: Int = 0,
    var senderName: String? = "",
    var receiverName: String? = "",
    var comment: String? = ""
): OperationAct(), Parcelable

fun OfficeInventory.toAccepted(): OfficeAcceptedInventory {
    return OfficeAcceptedInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        requestId = this.requestId,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        comment = this.comment
    )
}

fun OfficeInventory.toSent(): OfficeSentInventory {
    return OfficeSentInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        requestId = this.requestId,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        comment = this.comment
    )
}

fun OfficeInventory.toHistoryTransfer(): HistoryTransfer {
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
        status = HistoryEnum.AWAIT.status
    )
}


@Parcelize
data class OfficeAcceptedInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var requestId: String? = null,
    var storeUUID: String? = null,
    var quantity: Double? = null,
    var type: String? = null,
    var syncRequire: Int = 0,
    var senderName: String? = "",
    var comment: String? = ""
): OperationAct(), Parcelable


@Parcelize
data class OfficeSentInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var requestId: String? = null,
    var storeUUID: String? = null,
    var quantity: Double? = null,
    var type: String? = null,
    var syncRequire: Int = 0,
    var senderName: String? = "",
    var comment: String? = ""
): OperationAct(), Parcelable