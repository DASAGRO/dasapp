package kz.das.dasaccounting.ui.office

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeBottomNavigationVM: BaseVM(), KoinComponent {

    private val shiftRepository: ShiftRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private var isSend: Boolean = false

    init {
        refresh()
    }

    // TODO refresh add from ui
    fun refresh() {
        retrieve()
        initAwaitRequests()
    }

    fun initAwaitRequests() {
        viewModelScope.launch {
            try {
                if (!isSend) {
                    isSend = true
                    shiftRepository.initAwaitShiftStarted()
                    shiftRepository.initAwaitShiftFinished()
                    officeInventoryRepository.initAwaitAcceptInventory()
                    officeInventoryRepository.initAwaitSendInventory()
                }
            } catch (t: Throwable) {
                isSend = false
                throwableHandler.handle(t)
            } finally {
                isSend = false
            }
        }
    }

    private fun retrieve() {
        viewModelScope.launch {
            try {
                officeInventoryRepository.getOfficeMaterials()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }

    fun getAwaitSentOperationsLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

    fun getAwaitAcceptedOperationsLocally() = officeInventoryRepository.getOfficeAcceptedMaterialsLocally()

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

}