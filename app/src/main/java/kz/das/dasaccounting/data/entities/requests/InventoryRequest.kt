package kz.das.dasaccounting.data.entities.requests

import java.io.Serializable

open class InventorySendRequest(
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    var requestId: String? = null,
    var storeUUID: String? = null,
    val quantity: Double? = null,
    val type: String? = null,
    val senderName: String? = null
) : Serializable


open class InventoryGetRequest(
    val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    val senderUUID: String? = null,
    var requestId: String? = null,
    var storeUUID: String? = null,
    val quantity: Double? = null,
    val type: String? = null,
    val senderName: String? = null,
    var fileIds: Array<Any>?,
    var comment: String?
) : Serializable
