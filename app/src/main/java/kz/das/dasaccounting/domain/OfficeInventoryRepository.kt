package kz.das.dasaccounting.domain

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.domain.data.office.OfficeInventory

interface OfficeInventoryRepository {

    suspend fun getOfficeMaterials(): List<OfficeInventory>

    suspend fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>>

    suspend fun awaitAcceptInventory(officeInventory: OfficeInventory)

    suspend fun awaitSendInventory(officeInventory: OfficeInventory)

    suspend fun acceptInventory(officeInventory: OfficeInventory): ApiResponseMessage

    suspend fun sendInventory(officeInventory: OfficeInventory): ApiResponseMessage

    suspend fun syncInventoryMaterials()

}