package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.repositories.*
import kz.das.dasaccounting.domain.*
import org.koin.dsl.module

internal fun getRepositoriesModule() = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<ShiftRepository> { ShiftRepositoryImpl() }
    single<OfficeInventoryRepository> { OfficeInventoryRepositoryImpl() }
    single<DriverInventoryRepository> {DriverInventoryRepositoryImpl() }
}