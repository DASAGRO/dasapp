package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

@Parcelize
data class FligelProduct(
    var id: Int?,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Double?,
    var humidity: Int?,
    var name: String
): Parcelable, OperationAct()


fun FligelProduct.toAwait(): FligelAwaitProduct {
    return FligelAwaitProduct(
        id = this.id,
        combinerNumber = this.combinerNumber,
        comment = this.comment,
        fieldNumber = this.fieldNumber,
        harvestWeight = this.harvestWeight,
        humidity = this.humidity,
        name = this.name
    )
}

@Parcelize
data class FligelAwaitProduct(
    var id: Int?,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Double?,
    var humidity: Int?,
    var name: String
): Parcelable, OperationAct()