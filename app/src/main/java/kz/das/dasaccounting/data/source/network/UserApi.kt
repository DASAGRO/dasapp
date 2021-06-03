package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.ProfileEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/system/check/password")
    suspend fun checkPassword(@Body password: String): Response<Any>

    @POST("/api/system/update-password")
    suspend fun updatePassword(@Body password: String): Response<Any>

    @POST("/api/system/profile")
    suspend fun getProfile(): Response<ProfileEntity>

}