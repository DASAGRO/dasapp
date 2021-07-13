package kz.das.dasaccounting.ui.warehouse.operations

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject

class WarehouseOperationsVM: BaseVM() {

    private val warehouseInventoryRepository: WarehouseInventoryRepository by inject()
    private val inventoryList: ArrayList<OperationAct> = arrayListOf()

    private val inventoryLV = SingleLiveEvent<List<OperationAct>>()
    fun getInventories(): LiveData<List<OperationAct>> = inventoryLV

    fun getInventoryList() = inventoryList

    private var warehouseInventory: WarehouseInventory? = null

    fun setWarehouseInventory(warehouseInventory: WarehouseInventory?) {
        this.warehouseInventory = warehouseInventory
    }

    fun getWarehouseInventory(): WarehouseInventory? {
        return warehouseInventory
    }

    fun retrieve() {
        viewModelScope.launch {
            try {
                showLoading()
                inventoryList.clear()
                inventoryList.addAll(warehouseInventoryRepository.getWarehouseOfficeInventories(warehouseInventory?.storeUUID ?: ""))
                inventoryList.addAll(warehouseInventoryRepository.getWarehouseTransportAccessoryInventories(warehouseInventory?.storeUUID ?: ""))
                inventoryList.addAll(warehouseInventoryRepository.getWarehouseTransportInventories(warehouseInventory?.storeUUID ?: ""))
                inventoryLV.postValue(inventoryList)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }
}