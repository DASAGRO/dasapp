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

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    private val isOfficeInventorySentLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventorySent(): LiveData<Boolean> = isOfficeInventorySentLV

    fun getLocalInventory() = officeInventory

    fun setLocalInventory(officeInventory: OfficeInventory) {
        this.officeInventory = officeInventory
    }

    fun getUserRole() = userRepository.getUserRole()

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        this.officeInventory?.senderUUID = userRepository.getUser()?.userId
        this.officeInventory?.senderName = userRepository.getUser()?.lastName + " " +
                if (userRepository.getUser()?.firstName?.length ?: 0 > 0) {
                    userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    " "
                }  +

                if (userRepository.getUser()?.middleName?.length ?: 0 > 0) {
                    userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    " "
                }
        officeInventoryLV.postValue(officeInventory)
    }

    fun sendInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                officeInventory?.let {
                    officeInventoryRepository.sendInventory(it)
                    officeInventoryRepository.initCheckAwaitSentOperation(it)
                }
                isOfficeInventorySentLV.postValue(true)
            } catch (t: Throwable) {
                officeInventory?.let {
                    officeInventoryRepository.saveAwaitSentInventory(it)
                }
                isOnAwaitLV.postValue(true)
            } finally {
                hideLoading()
            }
        }
    }


}