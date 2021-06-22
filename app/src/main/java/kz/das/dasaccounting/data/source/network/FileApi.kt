package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.file.FileEntity
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {

    @Multipart
    @POST("files/upload")
    suspend fun uploadFile(@Part requestBody: MultipartBody.Part?): Response<FileEntity>

}