package kz.das.dasaccounting.ui.drivers.accept

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.inject

class AcceptInventoryInfoVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    private val transportInventoryLV = SingleLiveEvent<TransportInventory?>()
    fun getTransportInventory(): LiveData<TransportInventory?> = transportInventoryLV

    fun setTransportInventory(TransportInventory: TransportInventory?) = transportInventoryLV.postValue(TransportInventory)

    fun getLocation() = userRepository.getLastLocation()

}