package kz.das.dasaccounting.data.entities.common

import java.io.Serializable

data class InventoryRequest(
    val any: Any?,
    val fileIds: Array<Any>?,
    val comment: String
) : Serializable

data class InventoryGetRequest(
    val any: Any?
) : Serializable