package kz.das.dasaccounting.data.entities.requests

data class SendTransportRequest(
    val acceptedAt: Long?,
    val date: Long?,
    val id: Int?,
    val isAccepted: Long?,
    val isSend: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val sendAt: Long?,
    val senderName: String?,
    var requestId: String? = null,
    val syncRequire: Int?,
    val tcUUID: String?,
    val type: String?
)