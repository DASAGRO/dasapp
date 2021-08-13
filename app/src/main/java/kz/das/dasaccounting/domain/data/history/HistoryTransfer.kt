package kz.das.dasaccounting.domain.data.history

data class HistoryTransfer(
    val title: String,
    val descr: String,
    val date: Long,
    val dateText: String,
    val quantity: String,
    val senderName: String,
    val operationType: String,
    val isAwait: Boolean,
    val status: String
)

