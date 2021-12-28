package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import org.koin.core.inject

class ProfileHistoryVM: BaseVM() {
    private val driverRepository: DriverInventoryRepository by inject()
    private val officeRepository: OfficeInventoryRepository by inject()

    val isNeedRefresh: LiveData<Boolean> get() = _isNeedRefresh
    private val _isNeedRefresh = MutableLiveData<Boolean>()

    fun retrieve() {
        viewModelScope.launch {
            try {
                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()

                driverRepository.deleteDriverSentMaterialsLocally()
                officeRepository.deleteHistoryOfficeSentMaterialsLocally()

                driverRepository.deleteDriverAcceptedMaterialsLocally()
                officeRepository.deleteHistoryOfficeAcceptedMaterialsLocally()

                _isNeedRefresh.value = true
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }
}