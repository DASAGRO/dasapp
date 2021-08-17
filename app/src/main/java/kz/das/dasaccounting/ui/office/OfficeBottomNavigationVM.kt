package kz.das.dasaccounting.ui.office

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeBottomNavigationVM: BaseVM(), KoinComponent {

    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    init {
        refresh()
    }

    fun refresh() {
        initAwaitRequests()
        retrieve()
    }

//    fun initAwaitRequests() {
//        viewModelScope.launch {
//            try {
//                shiftRepository.initAwaitShiftStarted()
//                shiftRepository.initAwaitShiftFinished()
////                officeInventoryRepository.initAwaitAcceptInventory()
////                officeInventoryRepository.initAwaitSendInventory()
//            } catch (t: Throwable) {
//                throwableHandler.handle(t)
//            } finally {
//            }
//        }
//    }

    fun initAwaitRequests() {

        viewModelScope.launch {
            try {
                shiftRepository.initAwaitShiftStarted()
                shiftRepository.initAwaitShiftFinished()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
            }
        }

        officeInventoryRepository.getUnAcceptedInventories().forEach {
            sendAwaitUnAcceptedOfficeInventory(it)
        }

        officeInventoryRepository.getUnsentInventories().forEach {
            sendAwaitUnsentOfficeInventory(it)
        }
    }

    // Office send await requests
    private fun sendAwaitUnsentOfficeInventory(officeInventory: OfficeInventory) {
        viewModelScope.launch {
            try {
                officeInventoryRepository.sendInventory(officeInventory)
                officeInventoryRepository.removeUnsentInventory(officeInventory)
            } catch (t: Throwable) { }
        }
    }

    private fun sendAwaitUnAcceptedOfficeInventory(officeInventory: OfficeInventory) {
        viewModelScope.launch {
            try {
                officeInventoryRepository.acceptInventory(officeInventory, "Повторная отправка", arrayListOf())
                officeInventoryRepository.removeUnAcceptedInventory(officeInventory)
            } catch (t: Throwable) { }
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

    fun getAwaitAcceptedOperationsLocally() = officeInventoryRepository.getOfficeAcceptedMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

}