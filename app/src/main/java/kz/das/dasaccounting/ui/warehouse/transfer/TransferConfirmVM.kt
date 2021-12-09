package kz.das.dasaccounting.ui.warehouse.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.inject


class TransferConfirmVM: BaseVM() {
    private val warehouseInventoryRepository: WarehouseInventoryRepository by inject()

    private var warehouseInventory: WarehouseInventory? = null
    private var fileIds: ArrayList<Int>? = null

    fun setFileIds(fileIds: ArrayList<Int>) {
        this.fileIds = fileIds
    }

    fun userLocation() = userRepository.getLastLocation()

    fun getFileIds(): ArrayList<Int>? = this.fileIds

    fun getUserRole() = userRepository.getUserRole()

    fun getUser() = userRepository.getUser()

    fun getUserLocation() = userRepository.getLastLocation()

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()
    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    private val isWarehouseInventorySentLV = SingleLiveEvent<Boolean>()
    fun isWarehouseInventorySent(): LiveData<Boolean> = isWarehouseInventorySentLV

    fun setWarehouseInventory(warehouseInventory: WarehouseInventory?) {
        warehouseInventory?.date = System.currentTimeMillis()

        this.warehouseInventory = warehouseInventory
        warehouseInventoryLV.postValue(warehouseInventory)
    }

    fun getLocalInventory() = warehouseInventory

    fun sendInventory() {
        viewModelScope.launch {
            if (InternetAccess.internetCheck(context)) {
                showLoading()
                try {
                    warehouseInventory?.apply {
                        latitude = getUserLocation().lat
                        longitude = getUserLocation().long
                        writeObjectToLog(this.toString(), context)

                        warehouseInventoryRepository.sendInventory(this, fileIds)
                    }
                    isWarehouseInventorySentLV.postValue(true)
                } catch (t: Throwable) {
                    throwableHandler.handle(t)
                    isWarehouseInventorySentLV.postValue(false)
                } finally {
                    deleteSavedInventory()
                    hideLoading()
                }
            } else {
                isWarehouseInventorySentLV.postValue(false)
            }
        }
    }


}