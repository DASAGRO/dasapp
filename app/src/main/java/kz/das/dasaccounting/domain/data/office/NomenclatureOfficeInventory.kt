package kz.das.dasaccounting.domain.data.office

data class NomenclatureOfficeInventory(
    val id: Int = 0,
    val dateOfTake: String?,
    val fieldNumber: String,
    val materialUUID: String,
    val measurement: String?,
    val name: String?
)
