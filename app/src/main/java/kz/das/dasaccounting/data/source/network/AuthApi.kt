package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.ProfileEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/check/phone")
    suspend fun checkPhone(@Body phone: String?): Response<ProfileEntity>

    @POST("/api/auth/login")
    suspend fun login(@Body hashMap: HashMap<String, String?>): Response<ProfileEntity>

    @POST("/api/auth/send/password")
    suspend fun sendPassword(@Body phone: String?): Response<Any>
}