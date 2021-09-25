package kz.das.dasaccounting.data.entities.requests

import kz.das.dasaccounting.data.entities.office.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.io.Serializable

open class InventorySendRequest(
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var receiverUUID: String? = null,
    var qrData: String? = null,
    val quantity: Double? = null,
    val type: String? = null,
    val senderName: String? = null
) : Serializable

open class InventoryGetRequest(
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    val senderUUID: String? = null,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var qrData: String? = null,
    val quantity: Double? = null,
    val type: String? = null,
    val senderName: String? = null,
    var fileIds: Array<Any>?,
    var comment: String?
) : Serializable

fun OfficeInventory.toSendRequest(): InventorySendRequest {
    return InventorySendRequest(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        requestId = this.requestId,
        receiverUUID = this.receiverUUID,
        qrData = OfficeInventoryEntityTypeConvertor().officeInventoryToString(this.toEntity()),
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        senderName = this.senderName
    )
}

fun OfficeInventory.toGetRequest(comment: String ?= "",
                                 fileIds: ArrayList<Int> = arrayListOf()): InventoryGetRequest {
    return InventoryGetRequest(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        requestId = this.requestId,
        qrData = OfficeInventoryEntityTypeConvertor().officeInventoryToString(this.toEntity()),
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        senderName = this.senderName,
        fileIds = fileIds.toArray(),
        comment = comment ?: ""
    )
}