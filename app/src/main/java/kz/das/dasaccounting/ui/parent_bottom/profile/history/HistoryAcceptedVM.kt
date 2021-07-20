package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class HistoryAcceptedVM: BaseVM() {

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

    fun getHistoryWarehouseInventoriesLocally() = userRepository.getHistoryWarehouseInventoriesLocally()

    fun getHistoryTransportInventoriesLocally() = userRepository.getHistoryTransportInventoriesLocally()

    fun getHistoryOfficeInventoriesLocally() = userRepository.getHistoryOfficeInventoriesLocally()

    fun acceptedTransportInventoryLocally() = driverInventoryRepository.getDriverAcceptedMaterialsLocally()

    fun acceptedOfficeInventoryLocally() = officeInventoryRepository.getOfficeAcceptedMaterialsLocally()

}