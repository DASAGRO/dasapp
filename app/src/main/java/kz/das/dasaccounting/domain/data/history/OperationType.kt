package kz.das.dasaccounting.domain.data.history

enum class OperationType(val status: String) {
    WAREHOUSE("Warehouse inventory"),
    OFFICE("Office inventory"),
    DRIVER("Transport inventory")
}