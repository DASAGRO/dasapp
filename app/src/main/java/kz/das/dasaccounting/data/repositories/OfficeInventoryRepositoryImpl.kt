package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.ModulesLayer
import kz.das.dasaccounting.data.entities.common.InventoryRequest
import kz.das.dasaccounting.data.entities.office.*
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.OfficeOperationApi
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class OfficeInventoryRepositoryImpl: OfficeInventoryRepository {

    private val officeOperationApi: OfficeOperationApi by ModulesLayer.koin.inject()
    private val dasAppDatabase: DasAppDatabase by ModulesLayer.koin.inject()

    override suspend fun getOfficeMaterials(): List<OfficeInventory> {
        return officeOperationApi.getMaterials().unwrap ({ list -> list.map { it.toDomain() } },
        object : OnResponseCallback<List<OfficeInventoryEntity>> {
                override fun onSuccess(entity: List<OfficeInventoryEntity>) {
                    dasAppDatabase.officeInventoryDao().insertAllWithIgnore(entity)
                }
                override fun onFail(exception: Exception) { } // No handle require
            })
    }

    override suspend fun acceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>): Any {
        val inventoryRequest = InventoryRequest(officeInventory.toEntity(), fileIds.toArray(), comment)
        return officeOperationApi.acceptInventory(inventoryRequest)
            .unwrap(object : OnResponseCallback<ApiResponseMessage> {
                override fun onSuccess(entity: ApiResponseMessage) { } // No handle require
                override fun onFail(exception: Exception) {
                    if (exception is SocketTimeoutException
                        || exception is UnknownHostException
                        || exception is ConnectException) {
                        dasAppDatabase.officeInventoryAcceptedDao().insert(officeInventory.toAcceptedEntity().apply {
                            this.syncRequire == 1
                        })
                    }
                }
            })
    }

    override suspend fun sendInventory(officeInventory: OfficeInventory): Any {
        return officeOperationApi.sendInventory(InventoryRequest(officeInventory.toEntity(), null, "Отправка подтверждена"))
            .unwrap( object : OnResponseCallback<ApiResponseMessage> {
                override fun onSuccess(entity: ApiResponseMessage) { } // No handle require
                override fun onFail(exception: Exception) {
                    if (exception is SocketTimeoutException
                        || exception is UnknownHostException
                        || exception is ConnectException) {
                        dasAppDatabase.officeInventorySentDao().insert(officeInventory.toSentEntity().apply {
                            this.syncRequire == 1
                        })
                    }
                }
            })
    }

    override fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override suspend fun initAwaitAcceptInventory() {
        dasAppDatabase.officeInventoryAcceptedDao().all.forEach {
            officeOperationApi.acceptInventory(InventoryRequest(it, null, "Повторная отправка принятии"))
                .unwrap( object : OnResponseCallback<ApiResponseMessage> {
                    override fun onSuccess(entity: ApiResponseMessage) {
                        dasAppDatabase.officeInventoryAcceptedDao().removeItem(it)
                    }
                    override fun onFail(exception: Exception) { }
                })
        }
    }

    override suspend fun initAwaitSendInventory() {
        dasAppDatabase.officeInventorySentDao().all.forEach {
            officeOperationApi.sendInventory(InventoryRequest(it, null, "Повторная отправка передачи"))
                .unwrap( object : OnResponseCallback<ApiResponseMessage> {
                    override fun onSuccess(entity: ApiResponseMessage) {
                        dasAppDatabase.officeInventorySentDao().removeItem(it)
                    }
                    override fun onFail(exception: Exception) {}
                })
        }
    }

    override suspend fun syncInventoryMaterials() {
        // No procerue require
    }

    override fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventorySentDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryAcceptedDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }
}