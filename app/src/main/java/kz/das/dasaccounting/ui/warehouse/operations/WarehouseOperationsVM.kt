package kz.das.dasaccounting.ui.warehouse.operations

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory

class WarehouseOperationsVM: BaseVM() {


    private var warehouseInventory: WarehouseInventory? = null

    fun setWarehouseInventory(warehouseInventory: WarehouseInventory?) {
        this.warehouseInventory = warehouseInventory
    }

    fun getWarehouseInventory(): WarehouseInventory? {
        return warehouseInventory
    }

}