package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transport_accessory")
data class DriverInventoryAccessoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val comment: String? = null,
    val date: Long = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val receiverUUID: String? = null,
    val storeUUID: String? = null,
    val type: String? = null,
    val acceptedAt: Long? = 0,
    val sendAt: Long? = 0,
    val sealNumber: Int? = 0,
    val isSend: Int = 0,
    val isAccepted: Int = 0,
    val syncRequire: Int = 0
): Serializable
