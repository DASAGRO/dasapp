package kz.das.dasaccounting.domain.data.history

import kz.das.dasaccounting.domain.data.action.OperationAct

data class HistoryTransportInventory(
    val id: Long? = null,
    val dateTime: String? = null,
    val uuid: String? = null,
    val name: String? = null,
    val stateNumber: String? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val quantity: String? = null,
    val fullName: String? = null,
    val molUUID: String? = null,
    val status: String? = null
): OperationAct()