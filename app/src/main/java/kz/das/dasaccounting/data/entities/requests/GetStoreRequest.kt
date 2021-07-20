package kz.das.dasaccounting.data.entities.requests

data class GetStoreRequest(
    val comment: String,
    val date: Long,
    val fileIds: ArrayList<Int>?,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String?,
    val requestId: String?,
    val senderUUID: String?,
    val sealNumber: String?,
    val senderName: String?,
    val storeUUID: String?,
    val type: String?
)

