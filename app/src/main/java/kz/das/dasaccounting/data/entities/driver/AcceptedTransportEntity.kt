package kz.das.dasaccounting.data.entities.driver

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import java.io.Serializable
import java.util.*

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
    var storeUUID: String? = null,
    var senderUUID: String? = null,
    val stateNumber: String,
    val tsType: String,
    @PrimaryKey
    val uuid: String,
    val senderName: String?
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
        storeUUID = this.storeUUID,
        senderUUID = this.senderUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid,
        senderName = this.senderName
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
        storeUUID = this.storeUUID,
        senderUUID = this.senderUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid,
        senderName = this.senderName
    )
}