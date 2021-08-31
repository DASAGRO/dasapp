package kz.das.dasaccounting.di

import kz.das.dasaccounting.data.source.network.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal fun getApiModule() = module {
    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(UserApi::class.java) }
    single { get<Retrofit>().create(ShiftApi::class.java) }
    single { get<Retrofit>().create(FileApi::class.java) }
    single { get<Retrofit>().create(OfficeOperationApi::class.java) }
    single { get<Retrofit>().create(DriverOperationApi::class.java) }
    single { get<Retrofit>().create(WarehouseOperationApi::class.java) }
    single { get<Retrofit>().create(OperationHistoryApi::class.java) }
    single { get<Retrofit>(named("lateCallRetrofit")).create(LateCallApi::class.java) }
}