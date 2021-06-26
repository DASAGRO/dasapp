package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FligelProduct(
    var id: Int?,
    var combinerNumber: String?,
    var comment: String?,
    var fieldNumber: Int?,
    var harvestWeight: Int?,
    var humidity: Int?,
    var name: String
): Parcelable