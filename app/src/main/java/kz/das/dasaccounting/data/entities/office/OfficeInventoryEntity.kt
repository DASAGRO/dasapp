package kz.das.dasaccounting.data.entities.office

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.io.Serializable

@Entity(tableName = "materials")
data class OfficeInventoryEntity(
    val id: Int = 0,
    val date: Long = 0,
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @PrimaryKey
    val materialUUID: String,
    val senderUUID: String? = null,
    val receiverUUID: String? = null,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var quantity: Double? = null,
    val type: String? = null,
    val syncRequire: Int = 0,
    val senderName: String? = null,
    val receiverName: String? = null,
    var comment: String? = null
) : Serializable


fun OfficeInventoryEntity.toDomain(): OfficeInventory {
    return OfficeInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        receiverName = this.receiverName
    )
}

fun OfficeInventory.toEntity(): OfficeInventoryEntity {
    return OfficeInventoryEntity(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        receiverUUID = this.receiverUUID,
        requestId = this.requestId,
        storeUUIDSender = this.storeUUIDSender,
        storeUUIDReceiver = this.storeUUIDReceiver,
        quantity = this.quantity,
        type = this.type,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        receiverName = this.receiverName
    )
}