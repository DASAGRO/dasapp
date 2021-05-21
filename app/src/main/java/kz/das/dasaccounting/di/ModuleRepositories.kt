package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.repositories.AuthRepositoryImpl
import kz.das.dasaccounting.data.repositories.UserRepositoryImpl
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.UserRepository
import org.koin.dsl.module

internal fun getRepositoriesModule() = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
}