package kz.das.dasaccounting.domain.common

enum class InventoryType(val type: String) {
    OFFICE("office_inventory"),
    TRANSPORT("transport_inventory"),
    FLIGER("fligel_product"),
    WAREHOUSE("warehouse_inventory")
}