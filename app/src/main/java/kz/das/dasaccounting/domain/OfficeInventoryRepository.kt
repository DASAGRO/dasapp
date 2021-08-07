package kz.das.dasaccounting.domain

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory

interface OfficeInventoryRepository {

    suspend fun getOfficeMaterials(): List<OfficeInventory>

    suspend fun initAwaitAcceptInventory()

    suspend fun initAwaitSendInventory()

    suspend fun acceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>): Any

    suspend fun sendInventory(officeInventory: OfficeInventory): Any

    suspend fun saveAwaitAcceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>)

    suspend fun saveAwaitSentInventory(officeInventory: OfficeInventory)

    suspend fun getNomenclatures(): List<NomenclatureOfficeInventory>

    suspend fun saveOfficeInventory(officeInventory: OfficeInventory)

    fun getNomenclaturesLocally(): LiveData<List<NomenclatureOfficeInventory>>

    fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>>

    suspend fun initDeleteData()

    fun containsAwaitRequests(): Boolean

}