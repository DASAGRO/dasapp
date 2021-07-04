package kz.das.dasaccounting.domain.data.office

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.das.dasaccounting.domain.data.action.OperationAct

@Parcelize
data class OfficeInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
): OperationAct(), Parcelable

@Parcelize
data class OfficeAcceptedInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
): OperationAct(), Parcelable


@Parcelize
data class OfficeSentInventory(
    var id: Int = 0,
    var date: Long = 0,
    var name: String? = null,
    var humidity: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var materialUUID: String,
    var senderUUID: String? = null,
    var quantity: Int? = null,
    var type: String? = null,
    var acceptedAt: Long? = 0,
    var sendAt: Long? = 0,
    var syncRequire: Int = 0,
    var isSend: Int = 0,
    var senderName: String? = "",
    var comment: String? = "",
    var isAccepted: Int = 0
): OperationAct(), Parcelable