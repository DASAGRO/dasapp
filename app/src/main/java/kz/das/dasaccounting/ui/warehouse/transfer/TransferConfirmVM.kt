package kz.das.dasaccounting.ui.warehouse.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject


class TransferConfirmVM: BaseVM() {

    private val warehouseInventoryRepository: WarehouseInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var warehouseInventory: WarehouseInventory? = null
    private var fileIds: ArrayList<Int>? = null

    fun setFileIds(fileIds: ArrayList<Int>) {
        this.fileIds = fileIds
    }

    fun userLocation() = userRepository.getLastLocation()

    fun getFileIds(): ArrayList<Int>? = this.fileIds

    fun getUserRole() = userRepository.getUserRole()

    fun getUser() = userRepository.getUser()

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()
    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    private val isWarehouseInventorySentLV = SingleLiveEvent<Boolean>()
    fun isWarehouseInventorySent(): LiveData<Boolean> = isWarehouseInventorySentLV

    fun setWarehouseInventory(warehouseInventory: WarehouseInventory?) {
        this.warehouseInventory = warehouseInventory
        warehouseInventoryLV.postValue(warehouseInventory)
    }

    fun sendInventory() {
        viewModelScope.launch {
            showLoading()
            try {
                warehouseInventory?.let {

                    warehouseInventoryRepository.sendInventory(it, fileIds)
                }
                isWarehouseInventorySentLV.postValue(true)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
                isWarehouseInventorySentLV.postValue(false)
            } finally {
                hideLoading()
            }
        }
    }


}