package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.common.InventoryRequest
import kz.das.dasaccounting.data.entities.driver.TransportInventoryEntity
import kz.das.dasaccounting.data.entities.driver.toAcceptedEntity
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.driver.toSentEntity
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.DriverOperationApi
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class DriverInventoryRepositoryImpl: DriverInventoryRepository, KoinComponent {

    private val driverInventoryApi: DriverOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    override suspend fun getDriverTransports(): List<TransportInventory> {
        return driverInventoryApi.getTransports().unwrap ({ list -> list.map { it.toDomain() } },
            object : OnResponseCallback<List<TransportInventoryEntity>> {
                override fun onSuccess(entity: List<TransportInventoryEntity>) {
                    dasAppDatabase.driverInventoryDao().insertAll(entity)
                }
                override fun onFail(exception: Exception) { } // No handle require
            })
    }

    override suspend fun initAwaitAcceptInventory() {
//        dasAppDatabase.officeInventoryAcceptedDao().all.forEach {
//            driverInventoryApi.getInventoryDriverTransport()
//                .unwrap( object : OnResponseCallback<ApiResponseMessage> {
//                    override fun onSuccess(entity: ApiResponseMessage) {
//                        dasAppDatabase.officeInventoryAcceptedDao().removeItem(it)
//                    }
//                    override fun onFail(exception: Exception) { }
//                })
//        }
    }

    override suspend fun initAwaitSendInventory() {
//        dasAppDatabase.officeInventoryAcceptedDao().all.forEach {
//            driverInventoryApi.getInventoryDriverTransport()
//                .unwrap( object : OnResponseCallback<ApiResponseMessage> {
//                    override fun onSuccess(entity: ApiResponseMessage) {
//                        dasAppDatabase.officeInventoryAcceptedDao().removeItem(it)
//                    }
//                    override fun onFail(exception: Exception) { }
//                })
//        }
    }

    override suspend fun acceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ): Any {
        TODO("Not yet implemented")
    }

    override suspend fun sendInventory(transportInventory: TransportInventory): Any {
        TODO("Not yet implemented")
    }

    override suspend fun saveAwaitAcceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ) {
        dasAppDatabase.driverAcceptedInventoryDao().insert(transportInventory.toAcceptedEntity())
    }

    override suspend fun saveAwaitSentInventory(transportInventory: TransportInventory) {
        dasAppDatabase.driverSentInventoryDao().insert(transportInventory.toSentEntity())
    }

    override fun getTransportsLocally(): LiveData<List<TransportInventory>> {
        return dasAppDatabase.driverInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getDriverSentMaterialsLocally(): LiveData<List<TransportInventory>> {
        return dasAppDatabase.driverSentInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getDriverAcceptedMaterialsLocally(): LiveData<List<TransportInventory>> {
        return dasAppDatabase.driverAcceptedInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }
}