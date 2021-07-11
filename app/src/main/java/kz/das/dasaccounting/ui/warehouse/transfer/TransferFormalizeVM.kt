package kz.das.dasaccounting.ui.warehouse.transfer

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory


class TransferFormalizeVM: BaseVM() {

    private var warehouseInventory: WarehouseInventory? = null

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()

    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

}