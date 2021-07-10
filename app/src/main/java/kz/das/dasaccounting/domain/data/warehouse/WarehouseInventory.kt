package kz.das.dasaccounting.domain.data.warehouse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WarehouseInventory(
    val id: Int = 0,
    val date: Long = 0,
    var name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val senderUUID: String? = null,
    val sealNumber: String? = null,
    val storeUUID: String,
    val type: String? = null
): Parcelable