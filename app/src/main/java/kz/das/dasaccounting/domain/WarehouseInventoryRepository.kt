package kz.das.dasaccounting.domain

import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory


interface WarehouseInventoryRepository {

    suspend fun getWarehouseInventories(): List<WarehouseInventory>

    suspend fun getWarehouseOfficeInventories(uuid: String): List<OfficeInventory>

    suspend fun getWarehouseTransportInventories(uuid: String): List<TransportInventory>

    suspend fun getWarehouseTransportAccessoryInventories(uuid: String): List<TransportInventory>

    suspend fun acceptInventory(warehouseInventory: WarehouseInventory, comment: String, fileIds: ArrayList<Int>?): Any

    suspend fun sendInventory(warehouseInventory: WarehouseInventory): Any

    suspend fun sendInventory(warehouseInventory: WarehouseInventory, fileIds: ArrayList<Int>?): Any
}