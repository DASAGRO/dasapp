package kz.das.dasaccounting.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.das.dasaccounting.data.entities.driver.*
import kz.das.dasaccounting.data.entities.office.OfficeInventoryAcceptedEntity
import kz.das.dasaccounting.data.entities.office.OfficeInventorySentEntity
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.requests.*
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import kz.das.dasaccounting.data.source.network.LateCallApi
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import kz.das.dasaccounting.utils.AppConstants
import kz.das.dasaccounting.utils.AppConstants.Companion.SYNCED
import org.koin.core.KoinComponent
import org.koin.core.inject

class AwaitRequestInventoryRepositoryImpl: AwaitRequestInventoryRepository, KoinComponent {

    private val lateCallApi: LateCallApi by inject()
    private val dasAppDatabase: DasAppDatabase by inject()

    private fun getAwaitingOfficeInventoryAcceptedEntities(): List<OfficeInventoryAcceptedEntity> {
        return dasAppDatabase.officeInventoryAcceptedDao().all.filter { it.syncStatus == AppConstants.AWAITING }
    }

    private fun getAwaitingOfficeInventorySentEntities(): List<OfficeInventorySentEntity> {
        return dasAppDatabase.officeInventorySentDao().all.filter { it.syncStatus == AppConstants.AWAITING }
    }

    private fun getAwaitingAcceptedTransportEntities(): List<AcceptedTransportEntity> {
        return dasAppDatabase.driverAcceptedInventoryDao().all.filter { it.syncStatus == AppConstants.AWAITING }
    }

    private fun getAwaitingSentTransportEntities(): List<SentTransportEntity> {
        return dasAppDatabase.driverSentInventoryDao().all.filter { it.syncStatus == AppConstants.AWAITING }
    }

    private fun getAwaitingFligelProductEntities(): List<FligelProductEntity> {
        return dasAppDatabase.driverFligelDataDao().all.filter { it.syncStatus == AppConstants.AWAITING }
    }

    override suspend fun initAwaitRequests(): Flow<Any> {
        return flow {
            emit(
                lateCallApi.initAllAwaitRequests(
                    AwaitSendGetRequest(
                        getTMCList        = if (dasAppDatabase.officeInventoryAcceptedDao().all.isNotEmpty()) getAwaitingOfficeInventoryAcceptedEntities().map { it.toDomain().toGetRequest() } else null,
                        sendTMCList       = if (dasAppDatabase.officeInventorySentDao().all.isNotEmpty()) getAwaitingOfficeInventorySentEntities().map { it.toDomain().toSendRequest() } else null,
                        getTSList         = if (dasAppDatabase.driverAcceptedInventoryDao().all.isNotEmpty()) getAwaitingAcceptedTransportEntities().map { it.toDomain().toGetRequest("", arrayListOf()) } else null,
                        sendTSList        = if (dasAppDatabase.driverSentInventoryDao().all.isNotEmpty()) getAwaitingSentTransportEntities().map { it.toDomain().toSentRequest() } else null,
                        receiveFligelList = if (dasAppDatabase.driverFligelDataDao().all.isNotEmpty()) getAwaitingFligelProductEntities().map { it.toFligelProduct().toReceiveFligelDataRequest() } else null
                   )
                )
            )
        }
    }

    override suspend fun editSyncStatusAwaitRequests() {
        val officeInventoryAcceptedList = getAwaitingOfficeInventoryAcceptedEntities()
        val officeInventorySentList = getAwaitingOfficeInventorySentEntities()
        val driverAcceptedInventoryList = getAwaitingAcceptedTransportEntities()
        val driverSentInventoryList = getAwaitingSentTransportEntities()
        val driverFligelList = getAwaitingFligelProductEntities()


        if (officeInventoryAcceptedList.isNotEmpty()) {
            officeInventoryAcceptedList.forEach {
                it.syncStatus = SYNCED
                dasAppDatabase.officeInventoryAcceptedDao().updateEntity(it)
            }
        }

        if (officeInventorySentList.isNotEmpty()) {
            officeInventorySentList.forEach {
                it.syncStatus = SYNCED
                dasAppDatabase.officeInventorySentDao().updateEntity(it)
            }
        }

        if (driverAcceptedInventoryList.isNotEmpty()) {
            driverAcceptedInventoryList.forEach {
                it.syncStatus = SYNCED
                dasAppDatabase.driverAcceptedInventoryDao().updateEntity(it)
            }
        }

        if (driverSentInventoryList.isNotEmpty()) {
            driverSentInventoryList.forEach {
                it.syncStatus = SYNCED
                dasAppDatabase.driverSentInventoryDao().updateEntity(it)
            }
        }

        if (driverFligelList.isNotEmpty()) {
            driverFligelList.forEach {
                it.syncStatus = SYNCED
                dasAppDatabase.driverFligelDataDao().updateEntity(it)
            }
        }
    }
}