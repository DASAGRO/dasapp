package kz.das.dasaccounting.domain

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory

interface OfficeInventoryRepository {

    suspend fun getOfficeMaterials(): List<OfficeInventory>

    fun getUnsentInventories(): List<OfficeInventory>

    fun getUnAcceptedInventories(): List<OfficeInventory>

    suspend fun removeUnsentInventory(officeInventory: OfficeInventory)

    suspend fun removeUnAcceptedInventory(officeInventory: OfficeInventory)

    suspend fun acceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>): Any

    suspend fun sendInventory(officeInventory: OfficeInventory): Any

    suspend fun saveAwaitAcceptInventory(officeInventory: OfficeInventory, comment: String, fileIds: ArrayList<Int>)

    suspend fun saveAwaitSentInventory(officeInventory: OfficeInventory)

    suspend fun initCheckAwaitSentOperation(officeInventory: OfficeInventory)

    suspend fun initCheckAwaitAcceptOperation(officeInventory: OfficeInventory)

    suspend fun getNomenclatures(): List<NomenclatureOfficeInventory>

    suspend fun saveOfficeInventory(officeInventory: OfficeInventory)

    fun isEqualToLastFligelProduct(fligelProduct: FligelProduct): Boolean

    fun getNomenclaturesLocally(): LiveData<List<NomenclatureOfficeInventory>>

    fun getOfficeMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeSentMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getOfficeAcceptedMaterialsLocally(): LiveData<List<OfficeInventory>>

    fun getHistoryOfficeAcceptedMaterialsLocally(): LiveData<List<HistoryTransfer>>

    fun deleteHistoryOfficeAcceptedMaterialsLocally()

    fun getHistoryOfficeSentMaterialsLocally(): LiveData<List<HistoryTransfer>>

    fun deleteHistoryOfficeSentMaterialsLocally()

    suspend fun initDeleteData()

    fun containsAwaitRequests(): Boolean

}