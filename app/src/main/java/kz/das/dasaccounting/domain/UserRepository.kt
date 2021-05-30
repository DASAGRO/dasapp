package kz.das.dasaccounting.domain

import kz.das.dasaccounting.domain.data.Profile

interface UserRepository {

    fun updateToken(token: String)

    fun getToken(): String?

    fun getUser(): Profile?

    fun getUserRole(): String?

    fun setUser(profile: Profile)

    fun changeAvatar()

    fun deleteAvatar()

    fun isUserOnSession(): Boolean

    fun logOut()

    fun userOnWork(): Boolean

    fun startWork()

    fun finishWork()
}