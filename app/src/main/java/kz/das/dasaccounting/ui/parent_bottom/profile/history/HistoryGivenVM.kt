package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.extensions.zipLiveDataAny
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class HistoryGivenVM: BaseVM() {

    private val userRepository: UserRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    init {
        retrieve()
    }

    private fun retrieve() {
        viewModelScope.launch {
            try {
                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }

    fun getUser() = userRepository.getUser()

    fun getZippedHistory() = zipLiveDataAny(getHistoryWarehouseInventoriesLocally(),
        getHistoryTransportInventoriesLocally(),
        getHistoryOfficeInventoriesLocally(),
        sentTransportInventoryLocally(),
        sentOfficeInventoryLocally())

    private fun getHistoryWarehouseInventoriesLocally() = userRepository.getHistorySentWarehouseInventoriesLocally()

    private fun getHistoryTransportInventoriesLocally() = userRepository.getHistorySentTransportInventoriesLocally()

    private fun getHistoryOfficeInventoriesLocally() = userRepository.getHistorySentOfficeInventoriesLocally()

    private fun sentTransportInventoryLocally() = driverInventoryRepository.getDriverSentMaterialsLocally()

    private fun sentOfficeInventoryLocally() = officeInventoryRepository.getOfficeSentMaterialsLocally()

}