package kz.das.dasaccounting.data.source.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface OperationApi {

    @POST
    fun startWork(): Response<Any>

    @POST
    fun finishWork(): Response<Any>


    @GET
    fun getOperationList(): Response<Any>

    @POST
    fun makeTransfer(): Response<Any>

    @GET
    fun getReceivedTransfers(): Response<Any>

    @GET
    fun getAcceptedTransfers(): Response<Any>


}