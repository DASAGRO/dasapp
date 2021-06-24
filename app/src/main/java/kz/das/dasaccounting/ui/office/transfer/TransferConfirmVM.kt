package kz.das.dasaccounting.ui.office.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.inject

class TransferConfirmVM: BaseVM() {

    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        this.officeInventory?.receiverUUID = userRepository.getUser()?.userId
        this.officeInventory?.senderName = userRepository.getUser()?.lastName +
                userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] } + "." +
                userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }
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
                officeInventoryRepository.getOfficeMaterials()
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