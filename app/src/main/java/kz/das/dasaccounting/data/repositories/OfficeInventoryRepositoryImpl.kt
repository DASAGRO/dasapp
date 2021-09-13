package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.requests.InventoryGetRequest
import kz.das.dasaccounting.data.entities.requests.InventorySendRequest
import kz.das.dasaccounting.data.entities.office.*
import kz.das.dasaccounting.data.entities.requests.toGetRequest
import kz.das.dasaccounting.data.entities.requests.toSendRequest
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.OfficeOperationApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.compare
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.office.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeInventoryRepositoryImpl : OfficeInventoryRepository, KoinComponent {

    private val officeOperationApi: OfficeOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    private val userRepository: UserRepository by inject()
    private val userPreferences: UserPreferences by inject()

    override suspend fun getOfficeMaterials(): List<OfficeInventory> {
        return officeOperationApi.getMaterials().unwrap({ list -> list.map { it.toDomain() } },
            object : OnResponseCallback<List<OfficeInventoryEntity>> {
                override fun onSuccess(entity: List<OfficeInventoryEntity>) {
                    dasAppDatabase.officeInventoryDao().reload(entity)
                }

                override fun onFail(exception: Exception) {} // No handle require
            })
    }

    override suspend fun acceptInventory(
        officeInventory: OfficeInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ): Any {
        return officeOperationApi.acceptInventory(
            officeInventory.toGetRequest(comment, fileIds)
//            InventoryGetRequest(
//                id = officeInventory.id,
//                date = officeInventory.date,
//                name = officeInventory.name,
//                humidity = officeInventory.humidity,
//                latitude = officeInventory.latitude,
//                longitude = officeInventory.longitude,
//                materialUUID = officeInventory.materialUUID,
//                senderUUID = officeInventory.senderUUID,
//                requestId = officeInventory.requestId,
//                storeUUID = officeInventory.storeUUID,
//                quantity = officeInventory.quantity,
//                type = officeInventory.type,
//                senderName = officeInventory.senderName,
//                fileIds = fileIds.toArray(),
//                comment = comment
//            )
        ).unwrap()
    }

    override suspend fun sendInventory(officeInventory: OfficeInventory): Any {
        return officeOperationApi.sendInventory(
            officeInventory.toSendRequest()
//            InventorySendRequest(
//                id = officeInventory.id,
//                date = officeInventory.date,
//                name = officeInventory.name,
//                humidity = officeInventory.humidity,
//                latitude = officeInventory.latitude,
//                longitude = officeInventory.longitude,
//                materialUUID = officeInventory.materialUUID,
//                requestId = officeInventory.requestId,
//                storeUUID = officeInventory.storeUUID,
//                quantity = officeInventory.quantity,
//                type = officeInventory.type,
//                senderName = officeInventory.senderName
//            )
        ).unwrap()
    }

    override suspend fun saveAwaitAcceptInventory(
        officeInventory: OfficeInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ) {
        initCheckAwaitAcceptOperation(officeInventory)
        dasAppDatabase.officeInventoryAcceptedDao().insert(officeInventory.toAcceptedEntity())
    }

    override suspend fun initCheckAwaitAcceptOperation(officeInventory: OfficeInventory) {
        val dataItem = dasAppDatabase.officeInventoryDao().getItem(officeInventory.materialUUID)
        if (dataItem != null) {
            officeInventory.quantity?.let { cnt ->
                dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                dataItem.quantity = dataItem.quantity!! + cnt
                dasAppDatabase.officeInventoryDao().insert(dataItem)
            }
        } else {
            dasAppDatabase.officeInventoryDao().insert(officeInventory.toEntity())
        }
    }

    override suspend fun saveAwaitSentInventory(officeInventory: OfficeInventory) {
        initCheckAwaitSentOperation(officeInventory)
        dasAppDatabase.officeInventorySentDao().insert(officeInventory.toSentEntity())
    }

    override suspend fun initCheckAwaitSentOperation(officeInventory: OfficeInventory) {
        val dataItem = dasAppDatabase.officeInventoryDao().getItem(officeInventory.materialUUID)
        dataItem?.let {
            officeInventory.quantity?.let { cnt ->
                dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                dataItem.quantity = dataItem.quantity!! - cnt
                if (dataItem.quantity!! <= 0.0) {
                    dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                } else {
                    dasAppDatabase.officeInventoryDao().insert(dataItem)
                }
            }
        }
    }

    override suspend fun getNomenclatures(): List<NomenclatureOfficeInventory> {
        return officeOperationApi.getNomenclatures().unwrap({ list -> list.map { it.toDomain() } },
            object : OnResponseCallback<List<NomenclatureOfficeInventoryEntity>> {
                override fun onSuccess(entity: List<NomenclatureOfficeInventoryEntity>) {
                    dasAppDatabase.nomenclaturesDao().reload(entity)
                }

                override fun onFail(exception: Exception) {} // No handle require
            })
    }

    override fun getNomenclaturesLocally(): LiveData<List<NomenclatureOfficeInventory>> {
        return dasAppDatabase.nomenclaturesDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun isEqualToLastFligelProduct(fligelProduct: FligelProduct): Boolean {
        return userPreferences.getLastFligelProductCnt() > 3 && userPreferences.getLastFligelProduct()?.compare(fligelProduct) ?: false
    }

    override suspend fun saveOfficeInventory(officeInventory: OfficeInventory) {
        val dataItem = dasAppDatabase.officeInventoryDao().getItem(officeInventory.materialUUID)
        if (dataItem != null) {
            officeInventory.quantity?.let { cnt ->
                dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                dataItem.quantity = dataItem.quantity!! + cnt
                dasAppDatabase.officeInventoryDao().insert(dataItem)
            }
        } else {
            dasAppDatabase.officeInventoryDao().insert(officeInventory.toEntity())
        }
    }


    override fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getUnsentInventories(): List<OfficeInventory> {
        return dasAppDatabase.officeInventorySentDao().all.map { it.toDomain() }
    }

    override fun getUnAcceptedInventories(): List<OfficeInventory> {
        return dasAppDatabase.officeInventoryAcceptedDao().all.map { it.toDomain() }
    }

    override suspend fun removeUnsentInventory(officeInventory: OfficeInventory) {
        dasAppDatabase.officeInventorySentDao().removeItem(officeInventory.toSentEntity())
    }

    override suspend fun removeUnAcceptedInventory(officeInventory: OfficeInventory) {
        dasAppDatabase.officeInventoryAcceptedDao().removeItem(officeInventory.toAcceptedEntity())
    }


    //    override suspend fun initAwaitAcceptInventory() {
//        dasAppDatabase.officeInventoryAcceptedDao().all.forEach {
//            officeOperationApi.acceptInventory(
//                InventoryGetRequest(
//                    id = it.id,
//                    date = it.date,
//                    name = it.name,
//                    humidity = it.humidity,
//                    latitude = it.latitude,
//                    longitude = it.longitude,
//                    materialUUID = it.materialUUID,
//                    senderUUID = it.senderUUID,
//                    requestId = it.requestId,
//                    storeUUID = it.storeUUID,
//                    quantity = it.quantity,
//                    type = it.type,
//                    senderName = it.senderName,
//                    fileIds = null,
//                    comment = "Повторная отправка принятии"
//                )
//            )
//                .unwrap(object : OnResponseCallback<ApiResponseMessage> {
//                    override fun onSuccess(entity: ApiResponseMessage) {
//                        dasAppDatabase.officeInventoryAcceptedDao().removeItem(it)
//                    }
//
//                    override fun onFail(exception: Exception) {}
//                })
//        }
//    }
//
//    override suspend fun initAwaitSendInventory() {
//        dasAppDatabase.officeInventorySentDao().all.forEach {
//            officeOperationApi.sendInventory(
//                InventorySendRequest(
//                    id = it.id,
//                    date = it.date,
//                    name = it.name,
//                    humidity = it.humidity,
//                    latitude = userRepository.getLastLocation().lat,
//                    longitude = userRepository.getLastLocation().long,
//                    materialUUID = it.materialUUID,
//                    requestId = it.requestId,
//                    storeUUID = it.storeUUID,
//                    quantity = it.quantity,
//                    type = it.type,
//                    senderName = it.senderName
//                )
//            )
//                .unwrap(object : OnResponseCallback<ApiResponseMessage> {
//                    override fun onSuccess(entity: ApiResponseMessage) {
//                        dasAppDatabase.officeInventorySentDao().removeItem(it)
//                    }
//
//                    override fun onFail(exception: Exception) {}
//                })
//        }
//    }

    override fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventorySentDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryAcceptedDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getHistoryOfficeAcceptedMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.officeInventoryAcceptedDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override fun getHistoryOfficeSentMaterialsLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.officeInventorySentDao().allAsLiveData.map { it -> it.map { it.toDomain().toHistoryTransfer() } }
    }

    override suspend fun initDeleteData() {
        dasAppDatabase.officeInventoryAcceptedDao().removeAll()
        dasAppDatabase.officeInventorySentDao().removeAll()
        dasAppDatabase.officeInventoryDao().removeAll()
    }

    override fun containsAwaitRequests(): Boolean {
        return dasAppDatabase.officeInventoryAcceptedDao().all.isNotEmpty() ||
                dasAppDatabase.officeInventorySentDao().all.isNotEmpty()
    }
}