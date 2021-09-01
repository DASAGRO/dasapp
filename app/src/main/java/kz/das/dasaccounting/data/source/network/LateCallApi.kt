package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.requests.AwaitSendGetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LateCallApi {

    @POST("/api/goods/send-get/all")
    suspend fun initAllAwaitRequests(@Body awaitSendGetRequest: AwaitSendGetRequest): Response<Any>

}