package kz.das.dasaccounting.domain

interface AwaitRequestInventoryRepository {

    suspend fun initItems()

    suspend fun removeAllAwaitRequests()

}