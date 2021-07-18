package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import java.io.Serializable

@Entity(tableName = "accepted_transports")
data class AcceptedTransportEntity (
    val comment: String,
    val dateTime: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val model: String,
    val molUuid: String?,
    var requestId: String? = null,
    val stateNumber: String,
    val tsType: String,
    @PrimaryKey
    val uuid: String,
    val senderName: String?
    ): Serializable

fun AcceptedTransportEntity.toDomain(): TransportInventory {
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
        uuid = this.uuid,
        senderName = this.senderName
    )
}

fun TransportInventory.toAcceptedEntity(): AcceptedTransportEntity {
    return AcceptedTransportEntity(
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
        uuid = this.uuid,
        senderName = this.senderName
    )
}