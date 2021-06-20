package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.source.network.ShiftApi
import kz.das.dasaccounting.domain.ShiftRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShiftRepositoryImpl : ShiftRepository, KoinComponent {

    private val shiftApi: ShiftApi by inject()

    override suspend fun startShift(
        lat: Double,
        long: Double,
        time: Long,
        scannedQR: String?
    ): ApiResponseMessage {

        return shiftApi.startWork(
            hashMapOf(
                "latitude" to lat,
                "longitude" to long,
                "time" to time,
                "qr" to scannedQR
            )
        ).unwrap()
    }

    override suspend fun finishShift(
        lat: Double,
        long: Double,
        time: Long
    ): ApiResponseMessage {
        return shiftApi.finishWork(
            hashMapOf(
                "latitude" to lat,
                "longitude" to long,
                "time" to time
            )
        ).unwrap()
    }

}