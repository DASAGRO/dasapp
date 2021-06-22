package kz.das.dasaccounting.ui.office.accept

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class AcceptConfirmationVM : BaseVM(), KoinComponent {

    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        officeInventoryLV.postValue(officeInventory)
    }

    private val officeInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventoryAccepted(): LiveData<Boolean> = officeInventoryAcceptedLV

    fun acceptInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                officeInventory?.let {
                    officeInventoryRepository.acceptInventory(it)
                }
                officeInventoryAcceptedLV.postValue(true)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
                officeInventoryAcceptedLV.postValue(false)
            } finally {
                hideLoading()
            }
        }
    }

}
