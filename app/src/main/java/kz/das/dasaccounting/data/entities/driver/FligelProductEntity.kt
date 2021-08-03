package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.drivers.FligelProduct


@Entity(tableName = "fligel_products")
data class FligelProductEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Double?,
    var requestUUID: String?,
    var humidity: Int?,
    var name: String
)

fun FligelProductEntity.toFligelProduct(): FligelProduct {
    return FligelProduct(
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        fieldNumber = this.fieldNumber,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        requestUUID = this.requestUUID,
        id = this.id,
        name = this.name
    )
}

fun FligelProduct.toFligelProductEntity(): FligelProductEntity {
    return FligelProductEntity(
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        fieldNumber = this.fieldNumber,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        requestUUID = this.requestUUID,
        id = this.id ?: 0,
        name = this.name
    )
}