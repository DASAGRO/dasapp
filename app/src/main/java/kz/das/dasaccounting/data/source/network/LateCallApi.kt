package kz.das.dasaccounting.data.source.network

import retrofit2.Response
import retrofit2.http.POST

interface LateCallApi {

    @POST("/api/send-get/all")
    suspend fun initAllAwaitRequests(): Response<Void>

}