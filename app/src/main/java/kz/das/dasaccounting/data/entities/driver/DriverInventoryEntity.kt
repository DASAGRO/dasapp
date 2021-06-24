package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "driver_inventory")
data class DriverInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long = 0,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val tcUUID: String? = null,
    val name: String? = null,
    val receiverUUID: String? = null,
    val type: String? = null,
    val acceptedAt: Long? = 0,
    val sendAt: Long? = 0,
    val isSend: Int = 0,
    val isAccepted: Int = 0
): Serializable