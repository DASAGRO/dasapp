package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.common.ShiftRequest
import kz.das.dasaccounting.data.entities.common.toDomain
import kz.das.dasaccounting.data.source.network.ShiftApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.common.ShiftState
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShiftRepositoryImpl : ShiftRepository, KoinComponent {

    private val userPreferences: UserPreferences by inject()
    private val userRepository: UserRepository by inject()
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

    override suspend fun saveAwaitStartShift(
        lat: Double,
        long: Double,
        time: Long,
        scannedQR: String?
    ) {
        userRepository.startWork()
        userPreferences.setAwaitFinishWork(ShiftRequest(lat, long, time, scannedQR))
    }

    override suspend fun saveAwaitFinishShift(lat: Double, long: Double, time: Long) {
        userRepository.stopWork()
        userPreferences.setAwaitFinishWork(ShiftRequest(lat, long, time))
    }

    override suspend fun isShiftState(): ShiftState {
        return shiftApi.isOnSession().unwrap { it.toDomain() }
    }

    override suspend fun initAwaitShiftStarted() {
        userPreferences.getAwaitStartWork()?.let {
            startShift(it.latitude, it.longitude, it.time, it.qr)
        }
    }

    override suspend fun initAwaitShiftFinished() {
        userPreferences.getAwaitFinishWork()?.let {
            finishShift(it.latitude, it.longitude, it.time)
        }
    }
}