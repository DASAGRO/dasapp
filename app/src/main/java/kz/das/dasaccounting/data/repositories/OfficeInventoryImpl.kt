package kz.das.dasaccounting.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.NetworkSourceBound
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.office.toEntity
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.OfficeOperationApi
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class OfficeInventoryRepositoryImpl: OfficeInventoryRepository, KoinComponent {

    private val officeOperationApi: OfficeOperationApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    override suspend fun getOfficeMaterials(): List<OfficeInventory> {
        return officeOperationApi.getMaterials().unwrap( { list -> list.map { it.toDomain() }}, object : OnResponseCallback<List<OfficeInventoryEntity>> {
            override fun onSuccess(entity: List<OfficeInventoryEntity>) {
                dasAppDatabase.officeInventoryDao().insertAllWithIgnore(entity)
            }
            override fun onFail(exception: Exception) { } // No handle require
        })
    }

    override suspend fun acceptInventory(officeInventory: OfficeInventory): ApiResponseMessage {
        return officeOperationApi.acceptInventory(officeInventory.toEntity())
            .unwrap( object : OnResponseCallback<ApiResponseMessage> {
            override fun onSuccess(entity: ApiResponseMessage) {

            }

            override fun onFail(exception: Exception) {
                if (exception is SocketTimeoutException
                    || exception is UnknownHostException
                    || exception is ConnectException) {
                    dasAppDatabase.warehouseInventoryDao()
                }
            } // No handle require
        })
    }

    override suspend fun sendInventory(officeInventory: OfficeInventory): ApiResponseMessage {
        return officeOperationApi.sendInventory(officeInventory.toEntity())
            .unwrap( object : OnResponseCallback<ApiResponseMessage> {
                override fun onSuccess(entity: ApiResponseMessage) {

                }
                override fun onFail(exception: Exception) {

                } // No handle require
            })
    }

    override suspend fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>> {
        return dasAppDatabase.officeInventoryDao().allAsLiveData.map { it -> it.map { it.toDomain() } }
    }

    override suspend fun awaitAcceptInventory(officeInventory: OfficeInventory) {
        TODO("Not yet implemented")
    }

    override suspend fun awaitSendInventory(officeInventory: OfficeInventory) {
        TODO("Not yet implemented")
    }

    override suspend fun syncInventoryMaterials() {

    }
}