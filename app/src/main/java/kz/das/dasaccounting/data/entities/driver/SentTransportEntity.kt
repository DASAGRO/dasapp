package kz.das.dasaccounting.data.entities.driver

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.data.entities.requests.GetTransportRequest
import kz.das.dasaccounting.data.entities.requests.SendTransportRequest
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import java.io.Serializable

@Entity(tableName = "sent_transports")
data class SentTransportEntity(
    val comment: String,
    @Nullable
    val dateTime: Long?,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val model: String,
    val molUuid: String?,
    var requestId: String? = null,
    var storeUUID: String? = null,
    val stateNumber: String,
    val tsType: String,
    @PrimaryKey
    val uuid: String,
    val senderName: String?
): Serializable

fun SentTransportEntity.toDomain(): TransportInventory {
    return TransportInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun TransportInventory.toSentEntity(): SentTransportEntity {
    return SentTransportEntity(
        comment = this.comment,
        dateTime = this.dateTime ?: System.currentTimeMillis(),
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun TransportInventory.toSentRequest(): SendTransportRequest {
    return SendTransportRequest(
        date = this.dateTime,
        id =  this.id,
        isAccepted = 0,
        isSend = 0 ,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        sendAt = 0,
        senderName = this.senderName,
        requestId = this.requestId,
        syncRequire = 0,
        tcUUID = this.uuid,
        storeUUID = this.storeUUID,
        type = this.tsType,
        acceptedAt = 0
    )
}


fun TransportInventory.toGetRequest(comment: String, fileIds: ArrayList<Int>?): GetTransportRequest {
    return GetTransportRequest(
        date = this.dateTime ?: System.currentTimeMillis(),
        id =  this.id,
        isAccepted = 0,
        isSend = 0 ,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        sendAt = 0,
        senderName = this.senderName,
        requestId = this.requestId,
        syncRequire = 0,
        tcUUID = this.uuid,
        storeUUID = this.storeUUID,
        type = this.tsType,
        acceptedAt = 0,
        comment = comment,
        senderUUID = this.senderUUID,
        fileIds = fileIds
    )
}