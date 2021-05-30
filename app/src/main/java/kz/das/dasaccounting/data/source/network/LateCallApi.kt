package kz.das.dasaccounting.data.source.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface LateCallApi {

    @POST
    fun initPostRequest(@Body body: HashMap<Any, Any>, @Url link: String): Response<Any>

    @GET
    fun initPostRequest(@Url link: String): Response<Any>
    

}