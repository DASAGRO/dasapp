package kz.das.dasaccounting.ui.drivers

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import org.koin.core.inject

class DriverBottomNavigationVM : BaseVM() {

    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val awaitRequestInventoryRepository: AwaitRequestInventoryRepository by inject()

    init {
        refresh()
    }

    fun refresh() {
        retrieve()
        retrieveTransports()
        initAwaitRequests()
    }

    private fun retrieve() {
        if (officeInventoryRepository.getUnAcceptedInventories().isEmpty() &&
            officeInventoryRepository.getUnsentInventories().isEmpty() &&
            driverInventoryRepository.getUnsentInventories().isEmpty() &&
            driverInventoryRepository.getUnAcceptedInventories().isEmpty() &&
            driverInventoryRepository.getFligelData().isEmpty()
        ) {
            viewModelScope.launch {
                showLoading()
                delay(15000L)
                try {
                    officeInventoryRepository.getOfficeMaterials()
                } catch (t: Throwable) {
                    throwableHandler.handle(t)
                } finally {
                    hideLoading()
                }
            }
        }
    }

    private fun retrieveTransports() {
        if (officeInventoryRepository.getUnAcceptedInventories().isEmpty() &&
            officeInventoryRepository.getUnsentInventories().isEmpty() &&
            driverInventoryRepository.getUnsentInventories().isEmpty() &&
            driverInventoryRepository.getUnAcceptedInventories().isEmpty() &&
            driverInventoryRepository.getFligelData().isEmpty()
        ) {
            viewModelScope.launch {
                showLoading()
                delay(15000L)
                try {
                    driverInventoryRepository.getDriverTransports()
                } catch (t: Throwable) {
                    throwableHandler.handle(t)
                } finally {
                    hideLoading()
                }
            }
        }

    }

    fun initAwaitRequests() {
        viewModelScope.launch {
            try {
                shiftRepository.apply {
                    getAwaitShiftStarted()?.let {
                        startShift(it.latitude, it.longitude, it.time, it.qr)
                                .collect {
                                    clearAwaitStartWork()
                                    sendAwaitShiftFinish()
                                }
                    } ?: run {
                        sendAwaitShiftFinish()
                    }
                }
            } catch (t: Throwable) { throwableHandler.handle(t) }
        }

//        viewModelScope.launch {
//            try {
//                awaitRequestInventoryRepository.initAwaitRequests()
//                awaitRequestInventoryRepository.removeAllAwaitRequests()
//            } catch (t: Throwable) {
//                //throwableHandler.handle(t)
//            }
//        }

    }

    private suspend fun sendAwaitShiftFinish() {
        shiftRepository.apply {
            getAwaitShiftFinished()?.let {
                finishShift(it.latitude, it.longitude, it.time)
                        .collect {
                            clearAwaitFinishWork()
                        }
            }
        }
    }


    fun getAwaitSentOperationsLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

    fun getTransportsLocally() = driverInventoryRepository.getTransportsLocally()

    fun getAwaitSentTransportsLocally() = driverInventoryRepository.getDriverSentMaterialsLocally()

}