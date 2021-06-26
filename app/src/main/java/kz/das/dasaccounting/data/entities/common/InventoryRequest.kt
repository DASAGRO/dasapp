package kz.das.dasaccounting.data.entities.common

import java.io.Serializable

open class InventoryRequest(
    val id: Int = 0,
    val date: Long = 0,
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    val receiverUUID: String? = null,
    val quantity: Int? = null,
    val type: String? = null,
    val senderName: String? = null,
    var fileIds: Array<Any>?,
    var comment: String?
) : Serializable



open class InventorySendRequest(
    val id: Int = 0,
    val date: Long = 0,
    val name: String? = null,
    val humidity: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialUUID: String? = null,
    val receiverUUID: String? = null,
    val quantity: Int? = null,
    val type: String? = null,
    val senderName: String? = null
) : Serializable
