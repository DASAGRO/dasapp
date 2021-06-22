package kz.das.dasaccounting.ui.office.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.inject

class TransferConfirmVM: BaseVM() {

    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        officeInventoryLV.postValue(officeInventory)
    }

    private val isOfficeInventorySentLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventorySent(): LiveData<Boolean> = isOfficeInventorySentLV

    fun sendInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                officeInventory?.let {
                    officeInventoryRepository.sendInventory(it)
                }
                isOfficeInventorySentLV.postValue(true)
            } catch (t: Throwable) {
                isOfficeInventorySentLV.postValue(false)
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }


}