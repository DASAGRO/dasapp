package kz.das.dasaccounting.data.repositories

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.OnResponseCallback
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.core.ui.utils.getFileMultipart
import kz.das.dasaccounting.core.ui.utils.getImageFileMultipart
import kz.das.dasaccounting.data.entities.file.toDomain
import kz.das.dasaccounting.data.entities.history.*
import kz.das.dasaccounting.data.entities.toDomain
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.FileApi
import kz.das.dasaccounting.data.source.network.OperationHistoryApi
import kz.das.dasaccounting.data.source.network.UserApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.domain.data.file.File
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserRepositoryImpl: UserRepository, KoinComponent {

    private val context: Context by inject()
    private val userPreferences: UserPreferences by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val operationHistoryApi: OperationHistoryApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()
    
    private val userApi: UserApi by inject()
    private val fileApi: FileApi by inject()

    override fun updateToken(token: String) {
        userPreferences.setUserAccessToken(token)
    }

    override fun getToken() = userPreferences.getUserAccessToken()

    override fun getUser(): Profile? = userPreferences.getUser()

    override fun getUserRole(): String {
        return getUser()?.position ?: ""
    }

    override suspend fun getUserProfile(): Profile? {
        return userApi.getProfile().unwrap { it.toDomain() }
    }

    override suspend fun checkPassword(password: String): Any? {
        return userApi.checkPassword(password).unwrap()
    }

    override suspend fun updatePassword(password: String): Any? {
        return userApi.updatePassword(password)
    }

    override fun setUser(profile: Profile) {
        userPreferences.setUser(profile)
    }

    override suspend fun uploadFile(fileUri: Uri): File {
        return fileApi.uploadFile(getFileMultipart(context, fileUri)).unwrap { it.toDomain()}
    }

    override suspend fun sendSupport(fileIds: ArrayList<Int>, comment: String): ApiResponseMessage {
        return userApi.sendSupport(
            hashMapOf("fileIds" to fileIds.toArray(),
                        "comment" to comment)
        ).unwrap()
    }

    override suspend fun updateProfileImage(imageUri: Uri): String {
        val imageUrl = userApi.updateImage(getImageFileMultipart(context, imageUri)).body()?.message ?: ""
        userPreferences.setImagePath(imageUrl)
        return imageUrl
    }

    override fun isUserOnSession(): Boolean {
        return !userPreferences.getUserAccessToken().isNullOrEmpty()
    }

    override fun logOut() {
        userPreferences.clearUserAccessToken()

        userPreferences.clearUser()
    }

    override fun userOnWork(): Boolean {
        return userPreferences.isUserOnWork()
    }

    override fun startWork() {
        userPreferences.startWork()
    }

    override fun stopWork() {
        userPreferences.finishWork()
    }

    override fun saveLastLocation(location: Location) {
        userPreferences.saveLastLocation(location)
    }

    override fun getLastLocation(): Location = userPreferences.getLastLocation()

    override suspend fun deleteData() {
        userPreferences.clearUser()
        driverInventoryRepository.initDeleteData()
        officeInventoryRepository.initDeleteData()
        dasAppDatabase.historyOfficeInventoryDao().removeAll()
        dasAppDatabase.historyTransportInventoryDao().removeAll()
        dasAppDatabase.historyWarehouseInventoryDao().removeAll()
    }

    override suspend fun getHistoryWarehouseInventories(): List<HistoryTransfer> {
        return operationHistoryApi.getHistoryWarehouses().unwrap({ list -> list.map { it.toHistoryTransfer() } },
            object : OnResponseCallback<List<HistoryWarehouseInventoryEntity>> {
                override fun onSuccess(entity: List<HistoryWarehouseInventoryEntity>) {
                    dasAppDatabase.historyWarehouseInventoryDao().reload(entity)
                }

                override fun onFail(exception: Exception) {} // No handle require
            })
    }

    override suspend fun getHistoryOfficeInventories(): List<HistoryTransfer> {
        return operationHistoryApi.getHistoryOfficeInventories().unwrap({ list -> list.map { it.toHistoryTransfer() } },
            object : OnResponseCallback<List<HistoryOfficeInventoryEntity>> {
                override fun onSuccess(entity: List<HistoryOfficeInventoryEntity>) {
                    dasAppDatabase.historyOfficeInventoryDao().reload(entity)
                }
                override fun onFail(exception: Exception) {} // No handle require
            })
    }

    override suspend fun getHistoryTransportInventories(): List<HistoryTransfer> {
        return operationHistoryApi.getHistoryTransports().unwrap({ list -> list.map { it.toHistoryTransfer() } },
            object : OnResponseCallback<List<HistoryTransportInventoryEntity>> {
                override fun onSuccess(entity: List<HistoryTransportInventoryEntity>) {
                    dasAppDatabase.historyTransportInventoryDao().reload(entity)
                }
                override fun onFail(exception: Exception) {} // No handle require
            })
    }

    override fun getHistoryWarehouseInventoriesLocally(): LiveData<List<HistoryTransfer>>{
        return dasAppDatabase.historyWarehouseInventoryDao().allAsLiveData.map { list -> list.map { it.toHistoryTransfer() }}
    }

    override fun getHistoryOfficeInventoriesLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.historyOfficeInventoryDao().allAsLiveData.map { list -> list.map { it.toHistoryTransfer() }}
    }

    override fun getHistoryTransportInventoriesLocally(): LiveData<List<HistoryTransfer>> {
        return dasAppDatabase.historyTransportInventoryDao().allAsLiveData.map { list -> list.map { it.toHistoryTransfer() }}
    }
}