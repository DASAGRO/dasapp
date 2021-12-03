package kz.das.dasaccounting.domain

import android.net.Uri
import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.domain.data.action.InventoryRetain
import kz.das.dasaccounting.domain.data.file.File
import kz.das.dasaccounting.domain.data.history.HistoryTransfer

interface UserRepository {

    fun updateToken(token: String)

    fun getToken(): String?

    fun getUser(): Profile?

    suspend fun getUserProfile(): Profile?

    suspend fun updateProfileImage(imageUri: Uri): String

    suspend fun checkPassword(password: String): Any?

    suspend fun updatePassword(password: String): Any?

    suspend fun uploadFile(fileUri: Uri): File

    suspend fun sendSupport(fileIds: ArrayList<Int>, comment: String): ApiResponseMessage

    fun getUserRole(): String?

    fun setUser(profile: Profile)

    fun isUserOnSession(): Boolean

    fun logOut()

    fun userOnWork(): Boolean

    fun startWork()

    fun stopWork()

    fun saveLastLocation(location: Location)

    fun getLastLocation(): Location

    suspend fun deleteData()

    suspend fun getHistoryWarehouseInventories(): List<HistoryTransfer>

    suspend fun getHistoryOfficeInventories(): List<HistoryTransfer>

    suspend fun getHistoryTransportInventories(): List<HistoryTransfer>

    fun getHistorySentWarehouseInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun getHistorySentOfficeInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun getHistorySentTransportInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun getHistoryAcceptedWarehouseInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun getHistoryAcceptedOfficeInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun getHistoryAcceptedTransportInventoriesLocally(): LiveData<List<HistoryTransfer>>

    fun saveInventory(inventoryRetain: InventoryRetain)

    fun deleteSavedInventory()

    fun getSavedInventory(): InventoryRetain?

    fun saveStartQrScan(qrString: String)

    fun getStartQrScan(): String?

    fun deleteStartQrScan()
}