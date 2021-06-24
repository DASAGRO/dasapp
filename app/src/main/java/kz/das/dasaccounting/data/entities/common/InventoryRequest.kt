package kz.das.dasaccounting.data.entities.common

import java.io.Serializable

data class InventoryRequest(
    val any: Any?
): Serializable

data class InventoryGetRequest(
    val any: Any?,
    val fileIds: ArrayList<Int>
): Serializable