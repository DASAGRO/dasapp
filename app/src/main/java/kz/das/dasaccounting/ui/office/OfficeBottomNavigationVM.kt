package kz.das.dasaccounting.ui.office

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeBottomNavigationVM: BaseVM(), KoinComponent {

    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val awaitRequestInventoryRepository: AwaitRequestInventoryRepository by inject()

    init {
        refresh()
    }

    fun refresh() {
        initAwaitRequests()
        retrieve()
    }

    fun initAwaitRequests() {

        viewModelScope.launch {
            try {
                shiftRepository.apply {
                    getAwaitShiftStarted()?.let {
                        startShift(it.latitude, it.longitude, it.time, it.qr)
                                .collect {
                                    shiftRepository.clearAwaitStartWork()
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

    private fun retrieve() {
        if (officeInventoryRepository.getUnAcceptedInventories().isEmpty() &&
            officeInventoryRepository.getUnsentInventories().isEmpty()) {
            viewModelScope.launch {
                try {
                    showLoading()
                    delay(15000L)
                    officeInventoryRepository.getOfficeMaterials()
                } catch (t: Throwable) {
                    throwableHandler.handle(t)
                } finally {
                    hideLoading()
                }
            }
        }
    }

    fun getAwaitSentOperationsLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

}