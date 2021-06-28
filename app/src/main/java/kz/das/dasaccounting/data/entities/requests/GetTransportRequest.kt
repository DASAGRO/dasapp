package kz.das.dasaccounting.data.entities.requests

data class GetTransportRequest(
    val acceptedAt: Long,
    val comment: String,
    val date: Int,
    val fileIds: ArrayList<Int>?,
    val id: Int,
    val isAccepted: Int,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val senderUUID: String,
    val sendAt: Long,
    val senderName: String?,
    val syncRequire: Int,
    val tcUUID: String,
    val type: String
)