package kz.das.dasaccounting.data.repositories

import android.content.Context
import android.net.Uri
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.core.ui.utils.getFileMultipart
import kz.das.dasaccounting.core.ui.utils.getImageFileMultipart
import kz.das.dasaccounting.data.entities.file.toDomain
import kz.das.dasaccounting.data.entities.toDomain
import kz.das.dasaccounting.data.source.network.FileApi
import kz.das.dasaccounting.data.source.network.UserApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.domain.data.file.File
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserRepositoryImpl: UserRepository, KoinComponent {

    private val context: Context by inject()
    private val userPreferences: UserPreferences by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
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
        return userPreferences.isUserOnWork() ?: false
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

}