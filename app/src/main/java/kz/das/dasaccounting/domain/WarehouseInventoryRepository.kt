package kz.das.dasaccounting.domain

import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory


interface WarehouseInventoryRepository {

    suspend fun getWarehouseInventories(): List<WarehouseInventory>

    suspend fun acceptInventory(warehouseInventory: WarehouseInventory, comment: String, fileIds: ArrayList<Int>?): Any

    suspend fun sendInventory(warehouseInventory: WarehouseInventory): Any

}