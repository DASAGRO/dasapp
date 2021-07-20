package kz.das.dasaccounting.data.entities.requests

data class GetTransportRequest(
    val acceptedAt: Long,
    val comment: String,
    val date: Long,
    val fileIds: ArrayList<Int>?,
    val id: Int,
    val isAccepted: Int,
    val isSend: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val senderUUID: String,
    var requestId: String? = null,
    val sendAt: Long,
    val senderName: String?,
    val syncRequire: Int,
    val tcUUID: String,
    var storeUUID: String? = null,
    val type: String
)