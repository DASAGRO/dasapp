package kz.das.dasaccounting.ui.office.accept

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.inject

class AcceptInventoryInfoVM: BaseVM() {

    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    private val officeInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventoryAccepted(): LiveData<Boolean> = officeInventoryAcceptedLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        officeInventoryLV.postValue(officeInventory)
    }

    fun getLocation() = userRepository.getLastLocation()

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            officeInventory?.apply {
                latitude = getLocation().lat
                longitude = getLocation().long
                writeObjectToLog(this.toString(), context)

                officeInventoryRepository.saveAwaitAcceptInventory(this, comment, arrayListOf())
                isOnAwaitLV.postValue(true)

                startAwaitRequestWorker()
            }
        }
    }
}