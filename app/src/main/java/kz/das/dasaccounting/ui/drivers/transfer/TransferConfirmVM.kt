package kz.das.dasaccounting.ui.drivers.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.inject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TransferConfirmVM: BaseVM() {

    private val transportInventoryRepository: DriverInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var transportInventory: TransportInventory? = null

    private val transportInventoryLV = SingleLiveEvent<TransportInventory?>()
    fun getTransportInventory(): LiveData<TransportInventory?> = transportInventoryLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    fun getUserRole() = userRepository.getUserRole()

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

    private val isTransportInventorySentLV = SingleLiveEvent<Boolean>()
    fun isTransportInventorySent(): LiveData<Boolean> = isTransportInventorySentLV

    fun sendInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                transportInventory?.let {
                    transportInventoryRepository.sendInventory(it)
                }
                transportInventoryRepository.getDriverTransports()
                isTransportInventorySentLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    transportInventory?.let {
                        transportInventoryRepository.saveAwaitSentInventory(it)
                    }
                    isOnAwaitLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                    isTransportInventorySentLV.postValue(false)
                }
            } finally {
                hideLoading()
            }
        }
    }


}