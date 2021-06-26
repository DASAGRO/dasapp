package kz.das.dasaccounting.ui.drivers

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.inject

class DriverBottomNavigationVM: BaseVM() {

    private val userRepository: UserRepository by inject()
    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()

    private val operationsLV = SingleLiveEvent<ArrayList<OfficeInventory>>()
    fun getOperations(): LiveData<ArrayList<OfficeInventory>> = operationsLV

    init {
        refresh()
    }

    // TODO refresh add from ui
    fun refresh() {
        retrieve()
    }

    fun initAwaitRequests() {
        viewModelScope.launch {
            showLoading()
            try {
                shiftRepository.initAwaitShiftStarted()
                shiftRepository.initAwaitShiftFinished()
                officeInventoryRepository.initAwaitAcceptInventory()
                officeInventoryRepository.initAwaitSendInventory()
                driverInventoryRepository.initAwaitAcceptInventory()
                driverInventoryRepository.initAwaitSendInventory()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    private fun retrieve() {
        viewModelScope.launch {
            showLoading()
            try {
                val operationsArrayList: ArrayList<OfficeInventory> = arrayListOf()
                operationsArrayList.addAll(officeInventoryRepository.getOfficeMaterials())
                operationsLV.postValue(operationsArrayList)
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


}