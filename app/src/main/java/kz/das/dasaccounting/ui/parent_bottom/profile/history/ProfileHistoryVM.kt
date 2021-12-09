package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import org.koin.core.inject

class ProfileHistoryVM: BaseVM() {

    private val awaitRequestInventoryRepository: AwaitRequestInventoryRepository by inject()

    private val isRefreshLV = MutableLiveData<Boolean>()
    fun isRefresh(): LiveData<Boolean> = isRefreshLV

    fun setRefresh(refresh: Boolean) = isRefreshLV.postValue(refresh)

    init {
        retrieve()
    }

    fun retrieve() {
        viewModelScope.launch {
            try {
//                awaitRequestInventoryRepository.initAwaitRequests()
//                awaitRequestInventoryRepository.removeAllAwaitRequests()
                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()

                setRefresh(false)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }
}