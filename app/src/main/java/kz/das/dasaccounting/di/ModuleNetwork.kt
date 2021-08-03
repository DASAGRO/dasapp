package kz.das.dasaccounting.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.das.dasaccounting.data.source.network.interceptors.HeadersInterceptor
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal fun getNetworkModule() = module {
    single { provideHttpLoggingInterceptor() }
    single { provideHeadersInterceptor(get()) }
    single { provideGson() }
    single { provideOkHttpClient(get(), get()) }
    single { provideRetrofit(get(), get()) }
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    // TODO add http logging
    val level = if (true) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }

    return HttpLoggingInterceptor().apply { setLevel(level) }
}

private fun provideHeadersInterceptor(
    userPreferences: UserPreferences
): HeadersInterceptor {
    return HeadersInterceptor { userPreferences.getUserAccessToken() }
    //{ userPreferences.getUserRole()?.code }

}

private fun provideGson(): Gson {
    return GsonBuilder()
        .setDateFormat("")
        .serializeNulls()
        .create()
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    headersInterceptor: HeadersInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://app.dasagro.kz")
        //.baseUrl(GATEWAY_DEBUG_HOST)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}
