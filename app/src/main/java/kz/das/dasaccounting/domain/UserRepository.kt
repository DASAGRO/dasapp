package kz.das.dasaccounting.domain

import android.net.Uri
import kz.das.dasaccounting.domain.data.Profile
import java.io.File

interface UserRepository {

    fun updateToken(token: String)

    fun getToken(): String?

    fun getUser(): Profile?

    suspend fun getUserProfile(): Profile?

    suspend fun updateProfileImage(imageUri: Uri): String

    suspend fun checkPassword(password: String): Any?

    suspend fun updatePassword(password: String): Any?

    fun getUserRole(): String?

    fun setUser(profile: Profile)

    fun isUserOnSession(): Boolean

    fun logOut()

    fun userOnWork(): Boolean

    fun startWork()

    fun stopWork()
}