package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.source.network.AuthApi
import kz.das.dasaccounting.data.source.network.UserApi
import org.koin.dsl.module
import retrofit2.Retrofit

internal fun getApiModule() = module {
    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(UserApi::class.java) }
}