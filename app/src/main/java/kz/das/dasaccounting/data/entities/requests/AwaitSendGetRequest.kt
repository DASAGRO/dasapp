package kz.das.dasaccounting.data.entities.requests

data class AwaitSendGetRequest(
    val getTMCList: List<InventoryGetRequest>? = null,
    val sendTMCList: List<InventorySendRequest>? = null,
    val getTSList: List<GetTransportRequest>? = null,
    val sendTSList: List<SendTransportRequest>? = null,
    val receiveFligelList: List<ReceiveFligelDataRequest>? = null
)