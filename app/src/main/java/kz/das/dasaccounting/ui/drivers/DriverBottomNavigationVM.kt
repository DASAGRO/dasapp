package kz.das.dasaccounting.ui.drivers

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
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

//    fun initAwaitRequests() {
//        viewModelScope.launch {
//            try {
//                shiftRepository.initAwaitShiftStarted()
//                shiftRepository.initAwaitShiftFinished()
////                officeInventoryRepository.initAwaitAcceptInventory()
////                officeInventoryRepository.initAwaitSendInventory()
//                driverInventoryRepository.initAwaitAcceptInventory()
//                driverInventoryRepository.initAwaitSendInventory()
//                driverInventoryRepository.initAwaitReceiveFligerData()
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
            }
        }

        viewModelScope.launch {
            try {
                awaitRequestInventoryRepository.initAwaitRequests()
                awaitRequestInventoryRepository.removeAllAwaitRequests()
            } catch (t: Throwable) {
                //throwableHandler.handle(t)
            }
        }

//        officeInventoryRepository.getUnAcceptedInventories().forEach {
//            sendAwaitUnAcceptedOfficeInventory(it)
//        }
//
//        officeInventoryRepository.getUnsentInventories().forEach {
//            sendAwaitUnsentOfficeInventory(it)
//        }
//
//        driverInventoryRepository.getUnsentInventories().forEach {
//            sendAwaitUnsentTransportInventory(it)
//        }
//
//        driverInventoryRepository.getUnAcceptedInventories().forEach {
//            sendAwaitUnAcceptedTransportInventory(it)
//        }
//
//        driverInventoryRepository.getFligelData().forEach {
//            sendAwaitFligelTransportInventory(it)
//        }
    }

    // Office send await requests
    private fun sendAwaitUnsentOfficeInventory(officeInventory: OfficeInventory) {
        viewModelScope.launch {
            try {
                officeInventoryRepository.sendInventory(officeInventory)
                officeInventoryRepository.removeUnsentInventory(officeInventory)
            } catch (t: Throwable) {
            }
        }
    }

    private fun sendAwaitUnAcceptedOfficeInventory(officeInventory: OfficeInventory) {
        viewModelScope.launch {
            try {
                officeInventoryRepository.acceptInventory(
                    officeInventory,
                    "Повторная отправка",
                    arrayListOf()
                )
                officeInventoryRepository.removeUnAcceptedInventory(officeInventory)
            } catch (t: Throwable) {
            }
        }
    }

    // Driver send await requests
    private fun sendAwaitUnsentTransportInventory(driverInventory: TransportInventory) {
        viewModelScope.launch {
            try {
                driverInventoryRepository.sendInventory(driverInventory)
                driverInventoryRepository.removeUnsentInventory(driverInventory)
            } catch (t: Throwable) {
            }
        }
    }

    private fun sendAwaitUnAcceptedTransportInventory(driverInventory: TransportInventory) {
        viewModelScope.launch {
            try {
                driverInventoryRepository.acceptInventory(
                    driverInventory,
                    "Повторная отправка",
                    arrayListOf()
                )
                driverInventoryRepository.removeUnAcceptedInventory(driverInventory)
            } catch (t: Throwable) {
            }
        }
    }

    private fun sendAwaitFligelTransportInventory(fligelProduct: FligelProduct) {
        viewModelScope.launch {
            try {
                driverInventoryRepository.receiveFligelData(fligelProduct, arrayListOf())
                driverInventoryRepository.removeFligelData(fligelProduct)
            } catch (t: Throwable) {
            }
        }
    }


    fun getAwaitSentOperationsLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

    fun getAwaitAcceptedOperationsLocally() = officeInventoryRepository.getOfficeAcceptedMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

    fun getTransportsLocally() = driverInventoryRepository.getTransportsLocally()

    fun getAwaitSentTransportsLocally() = driverInventoryRepository.getDriverSentMaterialsLocally()

    fun getAwaitAcceptedTransportsLocally() =
        driverInventoryRepository.getDriverAcceptedMaterialsLocally()

    fun getAwaitFligelDataLocally() = driverInventoryRepository.getAwaitFligelDataLocally()
}