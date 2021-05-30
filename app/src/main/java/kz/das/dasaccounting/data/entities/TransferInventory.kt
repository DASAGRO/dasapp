package kz.das.dasaccounting.data.entities

open class TransferInventory {
    var inventoryId: Long? = null
    var title: String? = ""
    var desc: String? = ""
    var quantity: Long = 0
    var createdAt: String? = ""
    var transferredAt: String? = ""
    var isTransferred: Boolean = false
    var isAccepted: Boolean = false
}