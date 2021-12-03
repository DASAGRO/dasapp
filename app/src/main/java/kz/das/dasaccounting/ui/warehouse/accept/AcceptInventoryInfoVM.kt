package kz.das.dasaccounting.ui.warehouse.accept

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory

class AcceptInventoryInfoVM: BaseVM() {

    private var warehouseInventory: WarehouseInventory? = null

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()

    fun getUserLocation() = userRepository.getLastLocation()

    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

}