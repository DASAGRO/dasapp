package kz.das.dasaccounting.data.entities.office

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.io.Serializable

@Entity(tableName = "materials_sent")
data class OfficeInventorySentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0,
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    val senderUUID: String? = null,
    val quantity: Int? = null,
    val type: String? = null,
    val acceptedAt: Long? = 0,
    val sendAt: Long? = 0,
    val syncRequire: Int = 0,
    val senderName: String? = null,
    val isSend: Int = 0,
    val isAccepted: Int = 0
) : Serializable


fun OfficeInventorySentEntity.toDomain(): OfficeInventory {
    return OfficeInventory(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        quantity = this.quantity,
        type = this.type,
        acceptedAt = this.acceptedAt,
        sendAt = this.sendAt,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        isSend = this.isSend,
        isAccepted = this.isAccepted
    )
}

fun OfficeInventory.toSentEntity(): OfficeInventorySentEntity {
    return OfficeInventorySentEntity(
        id = this.id,
        date = this.date,
        name = this.name,
        humidity = this.humidity,
        latitude = this.latitude,
        longitude = this.longitude,
        materialUUID = this.materialUUID,
        senderUUID = this.senderUUID,
        quantity = this.quantity,
        type = this.type,
        acceptedAt = this.acceptedAt,
        sendAt = this.sendAt,
        syncRequire = this.syncRequire,
        senderName = this.senderName,
        isSend = this.isSend,
        isAccepted = this.isAccepted
    )
}