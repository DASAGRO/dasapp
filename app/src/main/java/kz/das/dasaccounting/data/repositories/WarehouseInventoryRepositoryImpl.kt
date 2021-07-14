package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.requests.GetStoreRequest
import kz.das.dasaccounting.data.entities.requests.SendStoreRequest
import kz.das.dasaccounting.data.entities.warehouse.toDomain
import kz.das.dasaccounting.data.source.network.WarehouseOperationApi
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class WarehouseInventoryRepositoryImpl: WarehouseInventoryRepository, KoinComponent {

    private val warehouseOperationApi: WarehouseOperationApi by inject()
    private val userRepository: UserRepository by inject()

    override suspend fun getWarehouseInventories(): List<WarehouseInventory> {
        return warehouseOperationApi.getWarehouses().unwrap { list -> list.map { it.toDomain() } }
    }

    override suspend fun getWarehouseOfficeInventories(uuid: String): List<OfficeInventory> {
        return warehouseOperationApi.getWarehousesOfficeInventories(uuid).unwrap { list -> list.map { it.toDomain() } }
    }

    override suspend fun getWarehouseTransportInventories(uuid: String): List<TransportInventory> {
        return warehouseOperationApi.getWarehousesTransportInventories(uuid).unwrap { list -> list.map { it.toDomain() } }
    }

    override suspend fun getWarehouseTransportAccessoryInventories(uuid: String): List<TransportInventory> {
        return warehouseOperationApi.getWarehousesTransportAccessoriesInventories(uuid).unwrap { list -> list.map { it.toDomain() } }
    }

    override suspend fun acceptInventory(
        warehouseInventory: WarehouseInventory,
        comment: String,
        fileIds: ArrayList<Int>?
    ): Any {
        return warehouseOperationApi.getWarehouse(
            GetStoreRequest(
                comment = comment,
                date = System.currentTimeMillis(),
                fileIds = fileIds,
                id = warehouseInventory.id,
                latitude = userRepository.getLastLocation().lat,
                longitude = userRepository.getLastLocation().long,
                name = warehouseInventory.name,
                requestId = warehouseInventory.requestId,
                receiverUUID = warehouseInventory.senderUUID,
                sealNumber = warehouseInventory.sealNumber,
                senderName = warehouseInventory.senderName,
                storeUUID = warehouseInventory.storeUUID,
                type = warehouseInventory.type
            )
        ).unwrap()
    }

    override suspend fun sendInventory(warehouseInventory: WarehouseInventory): Any {
        return warehouseOperationApi.sendWarehouse(
            SendStoreRequest(
                comment = "",
                date = System.currentTimeMillis(),
                fileIds = arrayListOf(),
                id = warehouseInventory.id,
                latitude = userRepository.getLastLocation().lat,
                longitude = userRepository.getLastLocation().long,
                name = warehouseInventory.name,
                requestId = warehouseInventory.requestId,
                receiverUUID = warehouseInventory.senderUUID,
                sealNumber = warehouseInventory.sealNumber,
                senderName = warehouseInventory.senderName,
                storeUUID = warehouseInventory.storeUUID,
                type = warehouseInventory.type
            )
        ).unwrap()
    }

    override suspend fun sendInventory(
        warehouseInventory: WarehouseInventory,
        fileIds: ArrayList<Int>?
    ): Any {
        return warehouseOperationApi.sendWarehouse(
            SendStoreRequest(
                comment = "",
                date = System.currentTimeMillis(),
                fileIds = fileIds ?: arrayListOf(),
                id = warehouseInventory.id,
                latitude = userRepository.getLastLocation().lat,
                longitude = userRepository.getLastLocation().long,
                name = warehouseInventory.name,
                requestId = warehouseInventory.requestId,
                receiverUUID = userRepository.getUser()?.userId,
                sealNumber = warehouseInventory.sealNumber,
                senderName = warehouseInventory.senderName,
                storeUUID = warehouseInventory.storeUUID,
                type = warehouseInventory.type
            )
        ).unwrap()
    }
}