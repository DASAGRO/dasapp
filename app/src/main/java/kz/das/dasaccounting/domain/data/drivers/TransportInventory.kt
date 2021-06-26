package kz.das.dasaccounting.domain.data.drivers

data class TransportInventory(
    var comment: String,
    var dateTime: String,
    var id: Int,
    var latitude: Int,
    var longitude: Int,
    var model: String,
    var molUuid: String,
    var stateNumber: String,
    var tsType: String,
    var senderName: String?,
    var uuid: String
)
