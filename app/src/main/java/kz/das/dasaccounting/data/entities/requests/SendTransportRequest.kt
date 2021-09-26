package kz.das.dasaccounting.data.entities.requests

data class SendTransportRequest(
    val date: Long?,
    val id: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val senderName: String?,
    var requestId: String? = null,
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var receiverUUID: String? = null,
    var senderUUID: String? = null,
    var qrData: String? = null,
    val syncRequire: Int?,
    val tcUUID: String?,
    val type: String?
)