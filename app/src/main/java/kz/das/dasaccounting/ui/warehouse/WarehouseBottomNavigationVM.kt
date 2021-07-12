package kz.das.dasaccounting.ui.warehouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject

class WarehouseBottomNavigationVM: BaseVM() {

    private val warehouseInventoryRepository: WarehouseInventoryRepository by inject()

    private val warehousesLV = SingleLiveEvent<List<WarehouseInventory>>()
    fun getWarehouses(): LiveData<List<WarehouseInventory>> = warehousesLV

    init {
        refresh()
    }

    fun refresh() {
        retrieve()
    }

    private fun retrieve() {
        viewModelScope.launch {
            try {
                val list = warehouseInventoryRepository.getWarehouseInventories()
                warehousesLV.postValue(list)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }

}