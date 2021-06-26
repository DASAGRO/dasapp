package kz.das.dasaccounting.data.entities.requests

data class GetFligelRequest(
    val acceptedAt: Any,
    val comment: String,
    val date: Long,
    val fileIds: List<Int>,
    val humidity: Int,
    val id: Int,
    val isAccepted: Any,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val materialUUID: String,
    val name: String,
    val quantity: Int,
    val receiverUUID: String,
    val sendAt: Long,
    val senderName: String,
    val syncRequire: Int,
    val type: String
)