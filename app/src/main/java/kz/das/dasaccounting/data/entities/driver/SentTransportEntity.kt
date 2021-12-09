package kz.das.dasaccounting.data.entities.driver

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.data.entities.requests.GetTransportRequest
import kz.das.dasaccounting.data.entities.requests.SendTransportRequest
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.drivers.TransportSentInventory
import java.io.Serializable
import kotlin.collections.ArrayList

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
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    val senderUUID: String? = null,
    val receiverUUID: String? = null,
    val receiverName: String? = null,
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
        receiverUUID = this.receiverUUID,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun SentTransportEntity.toSent(): TransportSentInventory {
    return TransportSentInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        receiverUUID = this.receiverUUID,
        receiverName = this.receiverName,
        senderUUID = this.senderUUID,
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
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        receiverUUID = this.receiverUUID,
        receiverName = this.receiverName,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        uuid = this.uuid
    )
}

fun TransportInventory.toSentRequest(): SendTransportRequest {
    return SendTransportRequest(
        date = this.dateTime,
        id =  this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        senderName = this.senderName,
        requestId = this.requestId,
        syncRequire = 0,
        tcUUID = this.uuid,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
//        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        qrData = DriverInventoryTypeConvertor().transportTransportToString(this.toEntity()),
        type = this.tsType
    )
}

fun TransportInventory.toGetRequest(comment: String, fileIds: ArrayList<Int>?): GetTransportRequest {
    return GetTransportRequest(
        date = this.dateTime ?: System.currentTimeMillis(),
        id =  this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.model,
        senderName = this.senderName,
        requestId = this.requestId,
        syncRequire = 0,
        tcUUID = this.uuid,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
//        receiverUUID = this.receiverUUID,
        qrData = DriverInventoryTypeConvertor().transportTransportToString(this.toEntity()),
        type = this.tsType,
        comment = comment,
        senderUUID = this.senderUUID,
        fileIds = fileIds
    )
}