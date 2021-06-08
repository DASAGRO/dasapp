package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.data.entities.ProfileEntity
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi {

    @POST("/api/system/check/password")
    suspend fun checkPassword(@Body password: String): Response<Any>

    @POST("/api/system/update-password")
    suspend fun updatePassword(@Body password: String): Response<Any>

    @POST("/api/system/profile")
    suspend fun getProfile(): Response<ProfileEntity>

    @Multipart
    @POST("/api/system/upload/image")
    suspend fun updateImage(@Part requestBody: MultipartBody.Part?): Response<ApiResponseMessage>
}