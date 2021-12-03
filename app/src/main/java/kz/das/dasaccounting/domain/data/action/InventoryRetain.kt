package kz.das.dasaccounting.domain.data.action

import kz.das.dasaccounting.domain.common.InventoryType

data class InventoryRetain(
        val type: InventoryType,
        val body: String
)