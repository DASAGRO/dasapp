package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class ProfileHistoryVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    init {
        retrieve()
    }

    fun retrieve() {
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
}