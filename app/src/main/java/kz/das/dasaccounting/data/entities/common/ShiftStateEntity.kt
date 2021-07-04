package kz.das.dasaccounting.data.entities.common

import kz.das.dasaccounting.domain.common.ShiftState
import java.io.Serializable

data class ShiftStateEntity(
    val opened: Boolean = false
): Serializable

fun ShiftStateEntity.toDomain(): ShiftState {
    return ShiftState(
        opened = this.opened
    )
}