package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.driver.*
import kz.das.dasaccounting.data.entities.requests.toReceiveFligelDataRequest
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.DriverOperationApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.drivers.compare
import kz.das.dasaccounting.domain.data.drivers.toHistoryTransfer
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import org.koin.core.KoinComponent
import org.koin.core.inject

class DriverInventoryRepositoryImpl : DriverInventoryRepository, KoinComponent {

    private val driverInventoryApi: DriverOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()
    private val userPreferences: UserPreferences by inject()

    override suspend fun getDriverTransports(): List<TransportInventory> {
        return driverInventoryApi.getTransports().unwrap({ list -> list.map { it.toDomain() } },
            object : OnResponseCallback<List<TransportInventoryEntity>> {
                override fun onSuccess(entity: List<TransportInventoryEntity>) {
                    dasAppDatabase.driverInventoryDao().reload(entity)
                }

                override fun onFail(exception: Exception) {} // No handle require
            })
    }


    override fun getUnsentInventories(): List<TransportInventory> {
        return dasAppDatabase.driverSentInventoryDao().all.map { it.toDomain() }
    }

    override fun getUnAcceptedInventories(): List<TransportInventory> {
        return dasAppDatabase.driverAcceptedInventoryDao().all.map { it.toDomain() }
    }

    override fun getFligelData(): List<FligelProduct> {
        return dasAppDatabase.driverFligelDataDao().all.map { it.toFligelProduct() }
    }

    override suspend fun removeFligelData(fligelProduct: FligelProduct) {
        dasAppDatabase.driverFligelDataDao().removeItem(fligelProduct.toFligelProductEntity())
    }

    override suspend fun removeUnsentInventory(transportInventory: TransportInventory) {
        dasAppDatabase.driverSentInventoryDao().removeItem(transportInventory.toSentEntity())
    }

    override suspend fun removeUnAcceptedInventory(transportInventory: TransportInventory) {
        dasAppDatabase.driverAcceptedInventoryDao().removeItem(transportInventory.toAcceptedEntity())
    }


//    override suspend fun initAwaitAcceptInventory() {
//        dasAppDatabase.driverAcceptedInventoryDao().all.forEach {
//            driverInventoryApi.getInventoryDriverTransport(
//                it.toDomain().toGetRequest("Повторная отправка", null)
//            )
//                .unwrap(object : OnResponseCallback<Any> {
//                    override fun onSuccess(entity: Any) {
//                        dasAppDatabase.driverAcceptedInventoryDao().removeItem(it)
//                    }
//
//                    override fun onFail(exception: Exception) {}
//                })
//        }
//    }

    //    override suspend fun initAwaitSendInventory() {
//        dasAppDatabase.driverSentInventoryDao().all.forEach {
//            driverInventoryApi.sendInventoryDriverTransport(it.toDomain().toSentRequest())
//                .unwrap(object : OnResponseCallback<Any> {
//                    override fun onSuccess(entity: Any) {
//                        dasAppDatabase.driverSentInventoryDao().removeItem(it)
//                    }
//
//                    override fun onFail(exception: Exception) {}
//                })
//        }
//    }
//
//    override suspend fun initAwaitReceiveFligerData() {
//        dasAppDatabase.driverFligelDataDao().all.forEach {
//            driverInventoryApi.receiveInventoryFligel(
//                it.toFligelProduct().toReceiveFligelDataRequest()
//            )
//                .unwrap(object : OnResponseCallback<Any> {
//                    override fun onSuccess(entity: Any) {
//                        dasAppDatabase.driverFligelDataDao().removeItem(it)
//                    }
//
//                    override fun onFail(exception: Exception) {}
//                })
//        }
//    }


    override suspend fun acceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>?
    ): Any {
        return driverInventoryApi.getInventoryDriverTransport(
            transportInventory.toGetRequest(
                comment,
                fileIds
            )
        ).unwrap()
    }

    override suspend fun sendInventory(transportInventory: TransportInventory): Any {
        return driverInventoryApi.sendInventoryDriverTransport(transportInventory.toSentRequest())
            .unwrap()
    }

    override suspend fun receiveFligelData(
        fligelProduct: FligelProduct,
        fileIds: ArrayList<Int>?
    ): Any {
        return driverInventoryApi.receiveInventoryFligel(
            fligelProduct.toReceiveFligelDataRequest(
                fileIds,
                ""
            )
        ).unwrap()
    }

    override suspend fun saveFligelProduct(fligelProduct: FligelProduct) {
        if (userPreferences.getLastFligelProduct()?.compare(fligelProduct) == false) {
            userPreferences.saveLastFligelProductCnt(0)
            userPreferences.saveLastFligelProduct(fligelProduct)
        } else {
            var cnt = userPreferences.getLastFligelProductCnt()
            cnt += 1
            userPreferences.saveLastFligelProductCnt(cnt)
        }
    }

    override suspend fun saveAwaitReceiveFligelData(fligelProduct: FligelProduct) {
        dasAppDatabase.driverFligelDataDao().insert(fligelProduct.toFligelProductEntity())
    }

    override suspend fun saveAwaitAcceptInventory(
        transportInventory: TransportInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ) {
        dasAppDatabase.driverInventoryDao().insert(transportInventory.toEntity())
        dasAppDatabase.driverAcceptedInventoryDao().insert(transportInventory.toAcceptedEntity())
    }

    override suspend fun saveAwaitSentInventory(transportInventory: TransportInventory) {
        dasAppDatabase.driverInventoryDao().removeItem(dasAppDatabase.driverInventoryDao().getItem(transportInventory.uuid))
        dasAppDatabase.driverSentInventoryDao().insertWithIgnore(transportInventory.toSentEntity())
    }

    override suspend fun removeItem(transportInventory: TransportInventory) {
        dasAppDatabase.driverInventoryDao().removeItem(dasAppDatabase.driverInventoryDao().getItem(transportInventory.uuid))
    }

    override suspend fun addItem(transportInventory: TransportInventory) {
        dasAppDatabase.driverInventoryDao().insert(transportInventory.toEntity())
    }

    override fun getTransportsLocally(): LiveData<List<TransportInventory>> {
        return dasAppDatabase.driverInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getDriverSentMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.driverSentInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override fun getDriverAcceptedMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.driverAcceptedInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override fun getHistoryDriverAcceptedMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.driverAcceptedInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override fun getHistoryDriverSentMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.driverSentInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override fun getAwaitFligelDataLocally(): LiveData<List<FligelProduct>> {
        return dasAppDatabase.driverFligelDataDao().allAsLiveData.map { it -> it.map { it.toFligelProduct() } }
    }

    override fun initDeleteData() {
        dasAppDatabase.driverInventoryDao().removeAll()
        dasAppDatabase.driverFligelDataDao().removeAll()
        dasAppDatabase.driverSentInventoryDao().removeAll()
        dasAppDatabase.driverAcceptedInventoryDao().removeAll()
    }

    override fun containsAwaitRequests(): Boolean {
        return dasAppDatabase.driverSentInventoryDao().all.isNotEmpty() ||
                    dasAppDatabase.driverAcceptedInventoryDao().all.isNotEmpty()
    }
}