package kz.das.dasaccounting.domain.data.drivers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

@Parcelize
data class TransportInventory(
    var comment: String,
    var dateTime: String,
    var id: Int,
    var latitude: Int,
    var longitude: Int,
    var model: String,
    var molUuid: String,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var uuid: String,
    var isPending: Boolean = false
): OperationAct(), Parcelable
