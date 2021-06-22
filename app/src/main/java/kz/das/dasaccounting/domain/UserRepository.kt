package kz.das.dasaccounting.domain

import android.net.Uri
import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.domain.data.file.File

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

}