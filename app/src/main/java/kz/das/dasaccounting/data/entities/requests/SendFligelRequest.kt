package kz.das.dasaccounting.data.entities.requests

data class SendFligelRequest(
    val acceptedAt: Long,
    val date: Long,
    val humidity: Int,
    val id: Int,
    val isAccepted: Int,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val materialUUID: String,
    val name: String,
    val quantity: Int,
    val sendAt: Long,
    val senderName: String,
    val syncRequire: Int,
    val type: String
)