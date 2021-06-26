package kz.das.dasaccounting.data.entities.requests

data class SendAccessoryTransportRequest(
    val acceptedAt: Long,
    val date: Long,
    val id: Int,
    val isAccepted: Long,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val poUUID: String,
    val sendAt: Long,
    val senderName: Int,
    val syncRequire: Int,
    val type: String
)