package kz.das.dasaccounting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM

class SplashVM: BaseVM() {

    private val isUserOkLV = SingleLiveEvent<Boolean>()
    fun isUserOk(): LiveData<Boolean> = isUserOkLV

    fun getUserRole() = userRepository.getUserRole()

    init {
        when {
            userRepository.isUserOnSession() && userRepository.isHistoryInventoriesNotEmpty() -> isUserOkLV.postValue(true)
            userRepository.isUserOnSession() && !userRepository.isHistoryInventoriesNotEmpty() -> loadHistoryInventories()
            else -> isUserOkLV.postValue(false)
        }

    }

    private fun loadHistoryInventories() {
        viewModelScope.launch {
            try {
                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()

                userRepository.setHistoryInventoriesLoaded(true)
                isUserOkLV.postValue(true)
            } catch (t: Throwable) {
                userRepository.setHistoryInventoriesLoaded(false)
                isUserOkLV.postValue(false)
            }
        }
    }

}