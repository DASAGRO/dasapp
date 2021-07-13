package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.TransportInventory

@Entity(tableName = "transports")
data class TransportInventoryEntity(
    val comment: String,
    val dateTime: String?,
    val id: Int,
    val latitude: Int,
    val longitude: Int,
    val model: String,
    val molUuid: String?,
    var requestId: String? = null,
    val stateNumber: String,
    val tsType: String,
    val senderName: String?,
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
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        senderName = this.senderName,
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
        stateNumber = this.stateNumber,
        tsType = this.tsType,
        uuid = this.uuid,
        senderName = this.senderName
    )
}