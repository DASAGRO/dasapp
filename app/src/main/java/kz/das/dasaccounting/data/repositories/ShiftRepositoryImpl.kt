package kz.das.dasaccounting.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    ): Flow<ApiResponseMessage> {

        return flow { emit(
                shiftApi.startWork(
                        hashMapOf(
                                "latitude" to lat,
                                "longitude" to long,
                                "time" to time,
                                "qr" to scannedQR
                        )
                )
        ) }
    }

    override suspend fun finishShift(
        lat: Double,
        long: Double,
        time: Long
    ): Flow<ApiResponseMessage> {

        return flow { emit(
            shiftApi.finishWork(
                    hashMapOf(
                            "latitude" to lat,
                            "longitude" to long,
                            "time" to time
                    )
            )
        )}
    }

    override suspend fun saveAwaitStartShift(
        lat: Double,
        long: Double,
        time: Long,
        scannedQR: String?
    ) {
        userRepository.startWork()
        userPreferences.setAwaitStartWork(ShiftRequest(lat, long, time, scannedQR))
    }

    override fun clearAwaitStartWork() {
        userPreferences.clearAwaitStartWork()
    }

    override suspend fun saveAwaitFinishShift(lat: Double, long: Double, time: Long) {
        userRepository.stopWork()
        userPreferences.setAwaitFinishWork(ShiftRequest(lat, long, time))
    }

    override fun clearAwaitFinishWork() {
        userPreferences.clearAwaitFinishWork()
    }

    override suspend fun isShiftState(): ShiftState {
        return shiftApi.isOnSession().unwrap { it.toDomain() }
    }

    override fun getAwaitShiftStarted(): ShiftRequest? {
        userPreferences.getAwaitStartWork()?.let {
            return it
        } ?: run { return null }
    }

    override fun getAwaitShiftFinished(): ShiftRequest? {
        userPreferences.getAwaitFinishWork()?.let {
            return it
        } ?: run { return null }
    }
}