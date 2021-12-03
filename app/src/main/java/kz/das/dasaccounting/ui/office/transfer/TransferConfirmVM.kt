package kz.das.dasaccounting.ui.office.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.data.entities.common.TransferItem
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.common.UserRole
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.inject

class TransferConfirmVM: BaseVM() {
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null
    private var generatedRequestId: String? = null

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

    fun setGeneratedRequestId(requestId: String) {
        this.generatedRequestId = requestId
    }

    fun getUserRole() = userRepository.getUserRole()

    fun getGeneratedRequestId() = generatedRequestId

    fun getUserLocation() = userRepository.getLastLocation()

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        officeInventory?.date = System.currentTimeMillis()
        if(userRepository.getUserRole() != UserRole.WAREHOUSE.role) {
            officeInventory?.storeUUIDSender = null
        }

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

    fun setTransferItem(transferItem: TransferItem?): OfficeInventory? {
        this.officeInventory?.receiverName = transferItem?.receiverName
        this.officeInventory?.receiverUUID = transferItem?.receiverUUID
        this.officeInventory?.storeUUIDReceiver = transferItem?.storeUUIDReceiver
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
        return this.officeInventory
    }

    fun sendInventory() {
        viewModelScope.launch {
            if (InternetAccess.internetCheck(context)) {
                showLoading()
                try {
                    officeInventory?.apply {
                        latitude = getUserLocation().lat
                        longitude = getUserLocation().long
                        writeObjectToLog(this.toString(), context)

                        officeInventoryRepository.sendInventory(this)
                        officeInventoryRepository.initCheckAwaitSentOperation(this)
                    }
                    isOfficeInventorySentLV.postValue(true)
                } catch (t: Throwable) {
                    officeInventory?.let {
                        officeInventoryRepository.saveAwaitSentInventory(it)
                    }
                    isOnAwaitLV.postValue(true)
                } finally {
                    deleteSavedInventory()
                    hideLoading()
                }
            } else {
                officeInventory?.let {
                    officeInventoryRepository.saveAwaitSentInventory(it)
                }
                isOnAwaitLV.postValue(true)
                deleteSavedInventory()
            }
        }
    }


}