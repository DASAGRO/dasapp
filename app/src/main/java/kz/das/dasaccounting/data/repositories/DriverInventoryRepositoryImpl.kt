package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.driver.*
import kz.das.dasaccounting.data.entities.requests.toReceiveFligelDataRequest
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.DriverOperationApi
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class DriverInventoryRepositoryImpl: DriverInventoryRepository, KoinComponent {

    private val driverInventoryApi: DriverOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()
    private val userRepository: UserRepository by inject()

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
        dasAppDatabase.driverAcceptedInventoryDao().all.forEach {
            driverInventoryApi.getInventoryDriverTransport(it.toDomain().toGetRequest(userRepository.getUser()?.userId ?: "", "Повторная отправка", null))
                .unwrap( object : OnResponseCallback<Any> {
                    override fun onSuccess(entity: Any) {
                        dasAppDatabase.driverAcceptedInventoryDao().removeItem(it)
                    }
                    override fun onFail(exception: Exception) { }
                })
        }
    }

    override suspend fun initAwaitSendInventory() {
        dasAppDatabase.driverSentInventoryDao().all.forEach {
            driverInventoryApi.sendInventoryDriverTransport(it.toDomain().toSentRequest())
                .unwrap( object : OnResponseCallback<Any> {
                    override fun onSuccess(entity: Any) {
                        dasAppDatabase.driverSentInventoryDao().removeItem(it)
                    }
                    override fun onFail(exception: Exception) { }
                })
        }
    }

    override suspend fun initAwaitReceiveFligerData() {
        dasAppDatabase.driverFligelDataDao().all.forEach {
            driverInventoryApi.receiveInventoryFligel(it.toFligelProduct().toReceiveFligelDataRequest())
                .unwrap( object : OnResponseCallback<Any> {
                    override fun onSuccess(entity: Any) {
                        dasAppDatabase.driverFligelDataDao().removeItem(it)
                    }
                    override fun onFail(exception: Exception) { }
                })
        }
    }


    override suspend fun acceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>?
    ): Any {
        return driverInventoryApi.getInventoryDriverTransport(transportInventory.toGetRequest(userRepository.getUser()?.userId ?: "", comment, fileIds)).unwrap()
    }

    override suspend fun sendInventory(transportInventory: TransportInventory): Any {
        return driverInventoryApi.sendInventoryDriverTransport(transportInventory.toSentRequest()).unwrap()
    }

    override suspend fun receiveFligelData(
        fligelProduct: FligelProduct,
        fileIds: ArrayList<Int>?
    ): Any {
        return driverInventoryApi.receiveInventoryFligel(fligelProduct.toReceiveFligelDataRequest(fileIds, "")).unwrap()
    }

    override suspend fun saveAwaitReceiveFligelData(fligelProduct: FligelProduct) {
        dasAppDatabase.driverFligelDataDao().insert(fligelProduct.toFligelProductEntity())
    }

    override suspend fun saveAwaitAcceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ) {
        dasAppDatabase.driverAcceptedInventoryDao().insert(transportInventory.toAcceptedEntity())
    }

    override suspend fun saveAwaitSentInventory(transportInventory: TransportInventory) {
        dasAppDatabase.driverSentInventoryDao().insertWithIgnore(transportInventory.toSentEntity())
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