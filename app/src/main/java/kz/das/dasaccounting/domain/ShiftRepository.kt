package kz.das.dasaccounting.domain

import kz.das.dasaccounting.core.extensions.ApiResponseMessage

interface ShiftRepository {

    suspend fun startShift(lat: Double, long: Double, time: Long, scannedQR: String? = null): ApiResponseMessage

    suspend fun finishShift(): ApiResponseMessage

}