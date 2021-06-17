package kz.das.dasaccounting.domain.data.drivers

data class Inventory(
    var inventoryId: Int = 0,
    var transferId: Int = 0,
    var title: String = "",
    var description: String = "",
    var quantity: Int = 0,
    var quantityTye: String = "",
    var createdAt: String = "",
    var acceptedAt: String = "",
    var transferState: Int = 0
)
