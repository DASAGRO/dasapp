package kz.das.dasaccounting.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.driver.toFligelProduct
import kz.das.dasaccounting.data.entities.driver.toGetRequest
import kz.das.dasaccounting.data.entities.driver.toSentRequest
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.requests.AwaitSendGetRequest
import kz.das.dasaccounting.data.entities.requests.toGetRequest
import kz.das.dasaccounting.data.entities.requests.toReceiveFligelDataRequest
import kz.das.dasaccounting.data.entities.requests.toSendRequest
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.LateCallApi
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AwaitRequestInventoryRepositoryImpl: AwaitRequestInventoryRepository, KoinComponent {

    private val lateCallApi: LateCallApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    // TODO Refactor as return method
    override suspend fun initAwaitRequests(): Flow<Any> {
        return flow {
            emit(
                lateCallApi.initAllAwaitRequests(
                    AwaitSendGetRequest(
                        getTMCList        = if (dasAppDatabase.officeInventoryAcceptedDao().all.isNotEmpty()) dasAppDatabase.officeInventoryAcceptedDao().all.map { it.toDomain().toGetRequest() } else null,
                        sendTMCList       = if (dasAppDatabase.officeInventorySentDao().all.isNotEmpty()) dasAppDatabase.officeInventorySentDao().all.map { it.toDomain().toSendRequest() } else null,
                        getTSList         = if (dasAppDatabase.driverAcceptedInventoryDao().all.isNotEmpty()) dasAppDatabase.driverAcceptedInventoryDao().all.map { it.toDomain().toGetRequest("", arrayListOf()) } else null,
                        sendTSList        = if (dasAppDatabase.driverSentInventoryDao().all.isNotEmpty()) dasAppDatabase.driverSentInventoryDao().all.map { it.toDomain().toSentRequest() } else null,
                        receiveFligelList = if (dasAppDatabase.driverFligelDataDao().all.isNotEmpty()) dasAppDatabase.driverFligelDataDao().all.map { it.toFligelProduct().toReceiveFligelDataRequest() } else null
                   )
                )
            )
        }
    }

    override suspend fun removeAllAwaitRequests() {
        dasAppDatabase.officeInventoryAcceptedDao().removeAll()
        dasAppDatabase.officeInventorySentDao().removeAll()
        dasAppDatabase.driverFligelDataDao().removeAll()
        dasAppDatabase.driverAcceptedInventoryDao().removeAll()
        dasAppDatabase.driverSentInventoryDao().removeAll()
    }

}