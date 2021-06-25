package kz.das.dasaccounting.data.entities.common

import java.io.Serializable

data class ShiftRequest(
    val latitude: Double,
    val longitude: Double,
    val time: Long,
    val qr: String? = null
): Serializable