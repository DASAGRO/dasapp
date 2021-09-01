package kz.das.dasaccounting.domain

interface AwaitRequestInventoryRepository {

    suspend fun initAwaitRequests(): Any

    suspend fun removeAllAwaitRequests()

}