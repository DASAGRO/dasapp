package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transports")
data class InventoryTransportEntity(
    val comment: String,
    val dateTime: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude: Int,
    val longitude: Int,
    val model: String,
    val molUuid: String,
    val stateNumber: String,
    val tsType: String,
    val uuid: String
)