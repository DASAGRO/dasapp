package kz.das.dasaccounting.data.entities.requests

data class SendTransportRequest(
    val acceptedAt: Long?,
    val date: Int?,
    val id: Int?,
    val isAccepted: Long?,
    val isSend: Int?,
    val latitude: Int?,
    val longitude: Int?,
    val name: String?,
    val sendAt: Long?,
    val senderName: String?,
    val syncRequire: Int?,
    val tcUUID: String?,
    val type: String?
)