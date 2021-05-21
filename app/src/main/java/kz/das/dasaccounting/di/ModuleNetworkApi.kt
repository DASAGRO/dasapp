package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.source.network.AuthApi
import org.koin.dsl.module
import retrofit2.Retrofit

internal fun getApiModule() = module {
    single { get<Retrofit>().create(AuthApi::class.java) }
}