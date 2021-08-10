package kz.das.dasaccounting.ui.warehouse.accept

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject

class AcceptInventoryInfoVM: BaseVM() {

    private var warehouseInventory: WarehouseInventory? = null
    private val userRepository: UserRepository by inject()

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()

    fun getUserLocation() = userRepository.getLastLocation()

    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

}