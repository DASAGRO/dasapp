package kz.das.dasaccounting.data.entities.requests

data class ReceiveFligelDataRequest(
    val acceptedAt: Int,
    val combinerNumber: String,
    val comment: String,
    val dateTime: Int,
    val fieldNumber: Int,
    val fileIds: List<Int>,
    val harvestWeight: Int,
    val humidity: Int,
    val id: Int,
    val isAccepted: Int,
    val isSend: Int,
    val name: String,
    val sendAt: Int,
    val senderName: String,
    val syncRequire: Int
)