package kz.das.dasaccounting.ui.warehouse.transfer

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject


class TransferFormalizeVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    private var warehouseInventory: WarehouseInventory? = null

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()

    fun getUserLocation() = userRepository.getLastLocation()

    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

}