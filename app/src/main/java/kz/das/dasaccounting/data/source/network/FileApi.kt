package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {

    @Multipart
    @POST("/api/system/upload")
    suspend fun updateImage(@Part requestBody: MultipartBody.Part?): Response<ApiResponseMessage>

}