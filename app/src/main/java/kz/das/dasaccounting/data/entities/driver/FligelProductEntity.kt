package kz.das.dasaccounting.data.entities.driver

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.core.extensions.getServerDateFromLong
import kz.das.dasaccounting.data.source.local.typeconvertors.FligelProductEntityTypeConverter
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.OperationType


@Entity(tableName = "fligel_products")
data class FligelProductEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Long,
    var latitude: Double,
    var longitude: Double,
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
        date = this.date,
        latitude = this.latitude,
        longitude = this.longitude,
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
        date = this.date ?: System.currentTimeMillis(),
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        name = this.name
    )
}

fun FligelProductEntity.toHistoryTransfer(): HistoryTransfer {
    return HistoryTransfer(
        title = this.name,
        descr = (
                "Номер комбайна: " + this.combinerNumber + "\n" +
                        "Номер поля: " + this.fieldNumber + "\n" +
                        "Вес урожая: " + this.harvestWeight),
        date = this.date ?: System.currentTimeMillis(),
        dateText = this.date.getServerDateFromLong() ?: "Ошибка даты",
        quantity = this.harvestWeight.toString(),
        senderName = "",
        operationType = OperationType.OFFICE.status,
        isAwait = false,
        qrData = FligelProductEntityTypeConverter().fligelProductEntityToString(this),
        status = HistoryEnum.AWAIT.status
    )
}