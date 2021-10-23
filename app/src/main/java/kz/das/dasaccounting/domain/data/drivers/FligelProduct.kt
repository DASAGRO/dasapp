package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

@Parcelize
data class FligelProduct(
    var id: Int?,
    var date: Long?,
    var longitude: Double?,
    var latitude: Double?,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Double?,
    var humidity: Int?,
    var requestUUID: String?,
    var name: String
): Parcelable, OperationAct()


fun FligelProduct.toAwait(): FligelAwaitProduct {
    return FligelAwaitProduct(
        id = this.id,
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        fieldNumber = this.fieldNumber,
        harvestWeight = this.harvestWeight,
        requestUUID = this.requestUUID,
        humidity = this.humidity,
        name = this.name
    )
}

fun FligelProduct.compareRepeat(fligelProduct: FligelProduct): Boolean {
    return this.combinerNumber == fligelProduct.combinerNumber &&
            this.fieldNumber == fligelProduct.fieldNumber &&
            this.harvestWeight == fligelProduct.harvestWeight
}

@Parcelize
data class FligelAwaitProduct(
    var id: Int?,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Double?,
    var requestUUID: String?,
    var humidity: Int?,
    var name: String
): Parcelable, OperationAct()