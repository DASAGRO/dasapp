package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.TransportInventory

@Entity(tableName = "transports")
data class TransportInventoryEntity(
    val comment: String,
    val dateTime: Long?,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val model: String,
    val molUuid: String?,
    var requestId: String? = null,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var storeUUID: String? = null,
    val stateNumber: String,
    val tsType: String,
    var senderName: String?,
    var receiverName: String?,
    @PrimaryKey
    val uuid: String
)

fun TransportInventoryEntity.toDomain(): TransportInventory {
    return TransportInventory(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
        receiverName = this.receiverName,
        uuid = this.uuid
    )
}

fun TransportInventory.toEntity(): TransportInventoryEntity {
    return TransportInventoryEntity(
        comment = this.comment,
        dateTime = this.dateTime,
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        model = this.model,
        molUuid = this.molUuid,
        requestId = this.requestId,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        storeUUID = this.storeUUID,
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid,
        senderName = this.senderName,
        receiverName = this.receiverName
    )
}