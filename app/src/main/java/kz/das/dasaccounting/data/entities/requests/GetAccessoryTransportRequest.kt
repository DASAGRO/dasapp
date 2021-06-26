package kz.das.dasaccounting.data.entities.requests

data class GetAccessoryTransportRequest(
    val acceptedAt: Long,
    val comment: String,
    val date: Long,
    val fileIds: List<Int>,
    val id: Int,
    val isAccepted: Long,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val poUUID: String,
    val receiverUUID: String,
    val sendAt: Long,
    val senderName: String,
    val syncRequire: Int,
    val type: String
)