package kz.das.dasaccounting.data.entities.common

data class TransferItem (
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    val senderUUID: String? = null,
    val receiverUUID: String? = null,
    val senderName: String? = null,
    val receiverName: String? = null
)