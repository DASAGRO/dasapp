package kz.das.dasaccounting.domain.data.office

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

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
    var requestId: String? = null,
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
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
        acceptedAt = this.acceptedAt,
        sendAt = this.sendAt,
        syncRequire = this.syncRequire,
        isSend = this.isSend,
        senderName = this.senderName,
        comment = this.comment,
        isAccepted = this.isAccepted
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
        acceptedAt = this.acceptedAt,
        sendAt = this.sendAt,
        syncRequire = this.syncRequire,
        isSend = this.isSend,
        senderName = this.senderName,
        comment = this.comment,
        isAccepted = this.isAccepted
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
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
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
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
): OperationAct(), Parcelable