package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory

class WarehouseInventoryRepositoryImpl: WarehouseInventoryRepository {


    override suspend fun getWarehouseInventories(): List<WarehouseInventory> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptInventory(
        warehouseInventory: WarehouseInventory,
        comment: String,
        fileIds: ArrayList<Int>?
    ): Any {
        TODO("Not yet implemented")
    }

    override suspend fun sendInventory(warehouseInventory: WarehouseInventory): Any {
        TODO("Not yet implemented")
    }
}