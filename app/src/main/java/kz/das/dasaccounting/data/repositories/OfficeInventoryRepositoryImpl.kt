package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.requests.InventoryGetRequest
import kz.das.dasaccounting.data.entities.requests.InventorySendRequest
import kz.das.dasaccounting.data.entities.office.*
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.OfficeOperationApi
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeInventoryRepositoryImpl : OfficeInventoryRepository, KoinComponent {

    private val officeOperationApi: OfficeOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    private val userRepository: UserRepository by inject()

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
            InventoryGetRequest(
                id = officeInventory.id,
                date = System.currentTimeMillis(),
                name = officeInventory.name,
                humidity = officeInventory.humidity,
                latitude = userRepository.getLastLocation().lat,
                longitude = userRepository.getLastLocation().long,
                materialUUID = officeInventory.materialUUID,
                senderUUID = officeInventory.senderUUID,
                requestId = officeInventory.requestId,
                storeUUID = officeInventory.storeUUID,
                quantity = officeInventory.quantity,
                type = officeInventory.type,
                senderName = officeInventory.senderName,
                fileIds = fileIds.toArray(),
                comment = comment
            )
        ).unwrap()
    }

    override suspend fun sendInventory(officeInventory: OfficeInventory): Any {
        return officeOperationApi.sendInventory(
            InventorySendRequest(
                id = officeInventory.id,
                date = System.currentTimeMillis(),
                name = officeInventory.name,
                humidity = officeInventory.humidity,
                latitude = userRepository.getLastLocation().lat,
                longitude = userRepository.getLastLocation().long,
                materialUUID = officeInventory.materialUUID,
                requestId = officeInventory.requestId,
                storeUUID = officeInventory.storeUUID,
                quantity = officeInventory.quantity,
                type = officeInventory.type,
                senderName = officeInventory.senderName
            )
        ).unwrap()
    }

    override suspend fun saveAwaitAcceptInventory(
        officeInventory: OfficeInventory,
        comment: String,
        fileIds: ArrayList<Int>
    ) {
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
        dasAppDatabase.officeInventoryAcceptedDao()
            .insert(officeInventory.toAcceptedEntity())
    }

    override suspend fun saveAwaitSentInventory(officeInventory: OfficeInventory) {
        val dataItem = dasAppDatabase.officeInventoryDao().getItem(officeInventory.materialUUID)
        dataItem?.let {
            officeInventory.quantity?.let { cnt ->
                dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                dataItem.quantity = dataItem.quantity!! - cnt
                if (dataItem.quantity == 0.0) {
                    dasAppDatabase.officeInventoryDao().removeItem(dataItem)
                } else {
                    dasAppDatabase.officeInventoryDao().insert(dataItem)
                }
            }
        }
        dasAppDatabase.officeInventorySentDao().insert(officeInventory.toSentEntity())
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

    override suspend fun initAwaitAcceptInventory() {
        dasAppDatabase.officeInventoryAcceptedDao().all.forEach {
            officeOperationApi.acceptInventory(
                InventoryGetRequest(
                    id = it.id,
                    date = System.currentTimeMillis(),
                    name = it.name,
                    humidity = it.humidity,
                    latitude = userRepository.getLastLocation().lat,
                    longitude = userRepository.getLastLocation().long,
                    materialUUID = it.materialUUID,
                    senderUUID = it.senderUUID,
                    requestId = it.requestId,
                    storeUUID = it.storeUUID,
                    quantity = it.quantity,
                    type = it.type,
                    senderName = it.senderName,
                    fileIds = null,
                    comment = "Повторная отправка принятии"
                )
            )
                .unwrap(object : OnResponseCallback<ApiResponseMessage> {
                    override fun onSuccess(entity: ApiResponseMessage) {
                        dasAppDatabase.officeInventoryAcceptedDao().removeItem(it)
                    }

                    override fun onFail(exception: Exception) {}
                })
        }
    }

    override suspend fun initAwaitSendInventory() {
        dasAppDatabase.officeInventorySentDao().all.forEach {
            officeOperationApi.sendInventory(
                InventorySendRequest(
                    id = it.id,
                    date = System.currentTimeMillis(),
                    name = it.name,
                    humidity = it.humidity,
                    latitude = userRepository.getLastLocation().lat,
                    longitude = userRepository.getLastLocation().long,
                    materialUUID = it.materialUUID,
                    requestId = it.requestId,
                    storeUUID = it.storeUUID,
                    quantity = it.quantity,
                    type = it.type,
                    senderName = it.senderName
                )
            )
                .unwrap(object : OnResponseCallback<ApiResponseMessage> {
                    override fun onSuccess(entity: ApiResponseMessage) {
                        dasAppDatabase.officeInventorySentDao().removeItem(it)
                    }

                    override fun onFail(exception: Exception) {}
                })
        }
    }

    override fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventorySentDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryAcceptedDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
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