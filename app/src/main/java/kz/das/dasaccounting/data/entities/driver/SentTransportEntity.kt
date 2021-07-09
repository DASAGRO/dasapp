package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.requests.GetTransportRequest
import kz.das.dasaccounting.data.entities.requests.SendTransportRequest
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import java.io.Serializable

@Entity(tableName = "sent_transports")
data class SentTransportEntity(
    val comment: String,
    val dateTime: String,
    val id: Int,
    val latitude: Int,
    val longitude: Int,
    val model: String,
    @PrimaryKey
    val molUuid: String,
    var requestId: String? = null,
    val stateNumber: String,
    val tsType: String,
    val uuid: String,
    val senderName: String?
): Serializable

fun SentTransportEntity.toDomain(): TransportInventory {
    return TransportInventory(
        comment = this.comment,
        dateTime = this.comment,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun TransportInventory.toSentEntity(): SentTransportEntity {
    return SentTransportEntity(
        comment = this.comment,
        dateTime = this.comment,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun TransportInventory.toSentRequest(): SendTransportRequest {
    return SendTransportRequest(
        date = System.currentTimeMillis(),
        id =  this.id,
        isAccepted = 0,
        isSend = 0 ,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        sendAt = 0,
        senderName = this.senderName,
        syncRequire = 0,
        tcUUID = this.uuid,
        type = this.tsType,
        acceptedAt = 0
    )
}


fun TransportInventory.toGetRequest(userId: String, comment: String, fileIds: ArrayList<Int>?): GetTransportRequest {
    return GetTransportRequest(
        date = System.currentTimeMillis(),
        id =  this.id,
        isAccepted = 0,
        isSend = 0 ,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        sendAt = 0,
        senderName = this.senderName,
        syncRequire = 0,
        tcUUID = this.uuid,
        type = this.tsType,
        acceptedAt = 0,
        comment = comment,
        senderUUID = userId,
        fileIds = fileIds
    )
}