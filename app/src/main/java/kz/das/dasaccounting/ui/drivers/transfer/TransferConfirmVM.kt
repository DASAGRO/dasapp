package kz.das.dasaccounting.ui.drivers.transfer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.data.entities.common.TransferItem
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.inject

class TransferConfirmVM: BaseVM() {
    private val context: Context by inject()

    private val transportInventoryRepository: DriverInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var transportInventory: TransportInventory? = null
    private var generatedRequestId: String? = null

    private val transportInventoryLV = SingleLiveEvent<TransportInventory?>()
    fun getTransportInventory(): LiveData<TransportInventory?> = transportInventoryLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    fun getUserRole() = userRepository.getUserRole()

    fun getUser() = userRepository.getUser()

    fun setLocalInventory(transportInventory: TransportInventory) {
        this.transportInventory = transportInventory
    }

    fun setGeneratedRequestId(requestId: String) {
        this.generatedRequestId = requestId
    }

    fun getLocalInventory() = transportInventory

    fun getGeneratedRequestId() = generatedRequestId

    fun setTransportInventory(transportInventory: TransportInventory?) {
        this.transportInventory = transportInventory
        this.transportInventory?.senderName = userRepository.getUser()?.lastName + " "
                if (userRepository.getUser()?.firstName?.length ?: 0 > 0) {
                    userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    ""
                }  +

                if (userRepository.getUser()?.middleName?.length ?: 0 > 0) {
                    userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    ""
                }
        transportInventoryLV.postValue(transportInventory)
    }

    fun setTransferItem(transferItem: TransferItem?): TransportInventory? {
        this.transportInventory?.receiverName = transferItem?.receiverName
        this.transportInventory?.receiverUUID = transferItem?.receiverUUID
        this.transportInventory?.storeUUIDReceiver = transferItem?.storeUUIDReceiver
        this.transportInventory?.senderName = userRepository.getUser()?.lastName + " "
        if (userRepository.getUser()?.firstName?.length ?: 0 > 0) {
            userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] }?.plus(".")
        } else {
            ""
        }  +

                if (userRepository.getUser()?.middleName?.length ?: 0 > 0) {
                    userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    ""
                }
        transportInventoryLV.postValue(transportInventory)
        return this.transportInventory
    }


    private val isTransportInventorySentLV = SingleLiveEvent<Boolean>()
    fun isTransportInventorySent(): LiveData<Boolean> = isTransportInventorySentLV

    fun sendInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                transportInventory?.let {
                    writeObjectToLog(it.toString(), context)

                    transportInventoryRepository.sendInventory(it)
                    transportInventoryRepository.removeItem(it)
                }
                isTransportInventorySentLV.postValue(true)
            } catch (t: Throwable) {
                transportInventory?.let {
                    transportInventoryRepository.saveAwaitSentInventory(it)
                }
                isOnAwaitLV.postValue(true)
            } finally {
                hideLoading()
            }
        }
    }


}