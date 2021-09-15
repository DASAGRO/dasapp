package kz.das.dasaccounting.domain.data.history

import kz.das.dasaccounting.domain.data.action.OperationAct

data class HistoryOfficeInventory (
    val id: Long? = null,
    val dateTime: String? = null,
    val materialUUID: String? = null,
    val name: String? = null,
    val measurement: String? = null,
    val quantity: String? = null,
    val humidity: String? = null,
    val molUUID: String? = null,
    val fullName: String? = null,
    val qrData: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val status: String? = null
): OperationAct()