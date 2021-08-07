package kz.das.dasaccounting.data.entities.requests

import kz.das.dasaccounting.data.entities.driver.FligelProductEntity
import kz.das.dasaccounting.domain.data.drivers.FligelProduct

data class ReceiveFligelDataRequest(
    val acceptedAt: Int,
    val combinerNumber: String?,
    val comment: String?,
    val date: Long?,
    val latitude: Double,
    val longitude: Double,
    val fieldNumber: Int?,
    val fileIds: ArrayList<Int>?,
    val harvestWeight: Double?,
    val humidity: Int?,
    val id: Int?,
    val isAccepted: Int?,
    val isSend: Int?,
    val name: String?,
    val sendAt: Int?,
    val requestId: String?,
    val senderName: String?,
    val syncRequire: Int?
)

fun ReceiveFligelDataRequest.toFligelProductEntity(): FligelProductEntity {
    return FligelProductEntity(
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        fieldNumber = this.fieldNumber,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        requestUUID = this.requestId,
        id = this.id ?: 0,
        date = this.date ?: 0,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.name ?: "Без названия"
    )
}

fun FligelProduct.toReceiveFligelDataRequest(fileIds: ArrayList<Int>?, senderName: String? = null): ReceiveFligelDataRequest {
    return ReceiveFligelDataRequest(
        acceptedAt = 0,
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        date = this.date,
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        fieldNumber = this.fieldNumber,
        fileIds = fileIds,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        requestId = this.requestUUID,
        isAccepted = 0,
        isSend = 0,
        name = "Тут должно быть название урожая",
        sendAt = 0,
        id = this.id,
        senderName = senderName,
        syncRequire = 0
    )
}


fun FligelProduct.toReceiveFligelDataRequest(): ReceiveFligelDataRequest {
    return ReceiveFligelDataRequest(
        acceptedAt = 0,
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        date = this.date,
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        fieldNumber = this.fieldNumber,
        fileIds = null,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        requestId = this.requestUUID,
        id = 0,
        isAccepted = 0,
        isSend = 0,
        name = "Тут должно быть название урожая",
        sendAt = 0,
        senderName = "senderName",
        syncRequire = 0
    )
}