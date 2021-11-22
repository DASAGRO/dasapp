package kz.das.dasaccounting.domain.data.office

import kz.das.dasaccounting.domain.common.ShiftType

data class QrSession(
    val id: Int,
    val uuid: String,
    val shift: String,
    val name: String
)