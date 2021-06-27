package kz.das.dasaccounting.data.entities.requests

data class GetStoreRequest(
    val acceptedAt: Int,
    val comment: String,
    val date: Int,
    val fileIds: ArrayList<Int>,
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