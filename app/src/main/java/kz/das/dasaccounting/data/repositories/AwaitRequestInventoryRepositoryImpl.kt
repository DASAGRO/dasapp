package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.source.network.LateCallApi
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AwaitRequestInventoryRepositoryImpl: AwaitRequestInventoryRepository, KoinComponent {

    private val lateCallApi: LateCallApi by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()

    // TODO Refactor as return method
    override suspend fun initItems() {
        lateCallApi.initAllAwaitRequests().unwrap()
    }

    override suspend fun removeAllAwaitRequests() {

    }
}