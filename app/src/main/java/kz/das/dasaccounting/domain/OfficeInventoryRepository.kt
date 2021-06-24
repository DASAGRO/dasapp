package kz.das.dasaccounting.domain

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.domain.data.office.OfficeInventory

interface OfficeInventoryRepository {

    suspend fun getOfficeMaterials(): List<OfficeInventory>

    suspend fun initAwaitAcceptInventory()

    suspend fun initAwaitSendInventory()

    suspend fun acceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>): Any

    suspend fun sendInventory(officeInventory: OfficeInventory): Any

    suspend fun syncInventoryMaterials()

    fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>>

}