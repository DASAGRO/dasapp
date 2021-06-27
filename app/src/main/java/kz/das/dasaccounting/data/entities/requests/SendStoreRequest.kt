package kz.das.dasaccounting.data.entities.requests

data class SendStoreRequest(
    val acceptedAt: Int,
    val date: Int,
    val id: Int,
    val isAccepted: Int,
    val isSend: Int,
    val latitude: Int,
    val longitude: Int,
    val name: String,
    val receiverUUID: String,
    val sealNumber: Int,
    val sendAt: Int,
    val senderName: String,
    val storeUUID: String,
    val syncRequire: Int,
    val type: String
)