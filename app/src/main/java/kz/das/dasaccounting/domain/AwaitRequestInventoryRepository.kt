package kz.das.dasaccounting.domain

import kotlinx.coroutines.flow.Flow

interface AwaitRequestInventoryRepository {

    suspend fun initAwaitRequests(): Flow<Any>

    suspend fun editSyncStatusAwaitRequests()

}