package kz.das.dasaccounting.data.entities.office

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory

@Entity(tableName = "nomenclatures")
data class NomenclatureOfficeInventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateOfTake: String?,
    val fieldNumber: String,
    val materialUUID: String,
    val measurement: String?,
    val name: String?
)

fun NomenclatureOfficeInventoryEntity.toDomain(): NomenclatureOfficeInventory {
    return NomenclatureOfficeInventory(
        id = this.id,
        dateOfTake = this.dateOfTake,
        fieldNumber = this.fieldNumber,
        materialUUID = this.materialUUID,
        measurement = this.measurement,
        name = this.name
    )
}