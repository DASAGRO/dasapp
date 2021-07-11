package kz.das.dasaccounting.ui.warehouse.operations

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory

class WarehouseTransferPickVM: BaseVM() {


    private var warehouseInventory: WarehouseInventory? = null

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()

    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

}