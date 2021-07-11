package kz.das.dasaccounting.data.entities.requests

data class SendStoreRequest(
    val comment: String?,
    val date: Long?,
    val id: Int?,
    val fileIds: ArrayList<Int>,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val receiverUUID: String?,
    val sealNumber: String?,
    val senderName: String?,
    val storeUUID: String?,
    val type: String?
)