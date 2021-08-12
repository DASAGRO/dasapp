package kz.das.dasaccounting.domain.data.warehouse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

@Parcelize
data class WarehouseInventory(
    val id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    val senderUUID: String? = null,
    var sealNumber: String? = null,
    var senderName: String? = null,
    var requestId: String? = null,
    var storeUUID: String,
    val type: String? = null
): OperationAct(), Parcelable