package kz.das.dasaccounting.data.entities.requests

import kz.das.dasaccounting.data.entities.driver.FligelProductEntity
import kz.das.dasaccounting.domain.data.drivers.FligelProduct

data class ReceiveFligelDataRequest(
    val acceptedAt: Int,
    val combinerNumber: String?,
    val comment: String?,
    val dateTime: Long?,
    val fieldNumber: Int?,
    val fileIds: ArrayList<Int>?,
    val harvestWeight: Int?,
    val humidity: Int?,
    val id: Int?,
    val isAccepted: Int?,
    val isSend: Int?,
    val name: String?,
    val sendAt: Int?,
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
        id = this.id ?: 0,
        name = this.name ?: "Без названия"
    )
}

fun FligelProduct.toReceiveFligelDataRequest(fileIds: ArrayList<Int>?, senderName: String? = null): ReceiveFligelDataRequest {
    return ReceiveFligelDataRequest(
        acceptedAt = 0,
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        dateTime = System.currentTimeMillis(),
        fieldNumber = this.fieldNumber,
        fileIds = fileIds,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
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
        dateTime = 0,
        fieldNumber = this.fieldNumber,
        fileIds = null,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        id = 0,
        isAccepted = 0,
        isSend = 0,
        name = "Тут должно быть название урожая",
        sendAt = 0,
        senderName = "senderName",
        syncRequire = 0
    )
}