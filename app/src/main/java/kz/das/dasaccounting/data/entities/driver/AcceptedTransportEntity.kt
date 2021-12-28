package kz.das.dasaccounting.data.entities.driver

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.TransportAcceptedInventory
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.utils.AppConstants.Companion.AWAITING
import java.io.Serializable

@Entity(tableName = "accepted_transports")
data class AcceptedTransportEntity (
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
    var senderUUID: String? = null,
    val receiverUUID: String? = null,
    val receiverName: String? = null,
    val stateNumber: String,
    val tsType: String,
    @PrimaryKey
    val uuid: String,
    val senderName: String?,
    var syncStatus: String = AWAITING
    ): Serializable

fun AcceptedTransportEntity.toDomain(): TransportInventory {
    return TransportInventory(
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
        receiverUUID = this.receiverUUID,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        receiverName = this.receiverName,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid
    )
}


fun AcceptedTransportEntity.toAccepted(): TransportAcceptedInventory {
    return TransportAcceptedInventory(
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
        receiverUUID = this.receiverUUID,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        receiverName = this.receiverName,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid,
        syncStatus = syncStatus
    )
}

fun TransportInventory.toAcceptedEntity(): AcceptedTransportEntity {
    return AcceptedTransportEntity(
        comment = this.comment,
        dateTime = this.dateTime ?: System.currentTimeMillis(),
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        receiverUUID = this.receiverUUID,
        receiverName = this.receiverName,
        senderUUID = this.senderUUID,
        senderName = this.senderName,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid
    )
}