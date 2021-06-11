package kz.das.dasaccounting.data.repositories

import android.content.Context
import android.net.Uri
import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.core.ui.utils.getFileMultipart
import kz.das.dasaccounting.data.entities.toDomain
import kz.das.dasaccounting.data.source.network.UserApi
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserRepositoryImpl: UserRepository, KoinComponent {

    private val context: Context by inject()
    private val userPreferences: UserPreferences by inject()
    private val userRepository: UserRepository by inject()
    private val userApi: UserApi by inject()

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

    override suspend fun updateProfileImage(imageUri: Uri): String {
        val imageUrl = userApi.updateImage(getFileMultipart(context, imageUri)).body()?.message ?: ""
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
}