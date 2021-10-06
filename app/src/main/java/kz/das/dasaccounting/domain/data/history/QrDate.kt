package kz.das.dasaccounting.domain.data.history

data class QrDateFull(
   val acceptedAt: Int? = null,
   val comment:String? = null,
   val date:Long? = null,
   val humidity: Int? = null,
   val id: Int? = null,
   val isAccepted: Int? = null,
   val isSend: Int? = null,
   val latitude: Double? = null,
   val longitude: Double? = null,
   val materialUUID: String? = null,
   val name: String? = null,
   val quantity: Double? = null,
   val requestId: String? = null,
   val sendAt: Int? = null,
   val senderName: String? = null,
   val senderUUID: String? = null,
   val storeUUIDSender: String? = null,
   val syncRequire: Int? = null,
   val type: String? = null
)

data class QrDateShort(
        val receiverName: String? = null,
        val receiverUUID: String? = null,
        val transferType: String? = null,
        val requestId: String? = null
)
