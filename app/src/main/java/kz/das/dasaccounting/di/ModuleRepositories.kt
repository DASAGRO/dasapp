package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.repositories.AuthRepositoryImpl
import kz.das.dasaccounting.data.repositories.OfficeInventoryRepositoryImpl
import kz.das.dasaccounting.data.repositories.ShiftRepositoryImpl
import kz.das.dasaccounting.data.repositories.UserRepositoryImpl
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.UserRepository
import org.koin.dsl.module

internal fun getRepositoriesModule() = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    single<ShiftRepository> { ShiftRepositoryImpl() }
    single<OfficeInventoryRepository> { OfficeInventoryRepositoryImpl() }
}