package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.data.source.preferences.UserPreferences
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserRepositoryImpl: UserRepository, KoinComponent {

    private val userPreferences: UserPreferences by inject()
    private val userRepository: UserRepository by inject()

    override fun updateToken(token: String) {
        userPreferences.setUserAccessToken(token)
    }

    override fun getToken() = userPreferences.getUserAccessToken()

    override fun getUser(): Profile? = userPreferences.getUser()

    override fun getUserRole(): String {
        return getUser()?.position ?: ""
    }

    override fun setUser(profile: Profile) {
        userPreferences.setUser(profile)
    }

    override fun changeAvatar() { }

    override fun deleteAvatar() { }

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