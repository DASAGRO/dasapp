package kz.das.dasaccounting.ui.drivers

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import org.koin.core.inject

class DriverBottomNavigationVM: BaseVM() {

    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()

    init {
        refresh()
    }

    // TODO refresh add from ui
    fun refresh() {
        retrieve()
        retrieveTransports()
    }

    fun initAwaitRequests() {
        viewModelScope.launch {
            try {
                shiftRepository.initAwaitShiftStarted()
                shiftRepository.initAwaitShiftFinished()
                officeInventoryRepository.initAwaitAcceptInventory()
                officeInventoryRepository.initAwaitSendInventory()
                driverInventoryRepository.initAwaitAcceptInventory()
                driverInventoryRepository.initAwaitSendInventory()
                driverInventoryRepository.initAwaitReceiveFligerData()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
            }
        }
    }

    private fun retrieve() {
        viewModelScope.launch {
            showLoading()
            try {
                officeInventoryRepository.getOfficeMaterials()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    private fun retrieveTransports() {
        viewModelScope.launch {
            showLoading()
            try {
                driverInventoryRepository.getDriverTransports()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    fun getAwaitSentOperationsLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

    fun getAwaitAcceptedOperationsLocally() = officeInventoryRepository.getOfficeAcceptedMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

    fun getTransportsLocally() = driverInventoryRepository.getTransportsLocally()

    fun getAwaitSentTransportsLocally() = driverInventoryRepository.getDriverSentMaterialsLocally()

    fun getAwaitAcceptedTransportsLocally() = driverInventoryRepository.getDriverAcceptedMaterialsLocally()

    fun getAwaitFligelDataLocally() = driverInventoryRepository.getAwaitFligelDataLocally()
}