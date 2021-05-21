package kz.das.dasaccounting.di

import android.content.Context
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

class DataLayer {
    val okHttpClient: OkHttpClient by koin.inject()

    companion object {
        private lateinit var instance: DataLayer

        lateinit var koin: Koin

        fun init(
            applicationContext: Context,
            deviceId: String,
            deviceName: String
        ) {
            Hawk.init(applicationContext).build()
            koin = koinApplication {
                modules(provideModules(applicationContext, deviceId, deviceName))
            }.koin

            instance = DataLayer()
        }

        fun getInstance(): DataLayer {
            if (!::instance.isInitialized) {
                throw Exception("Data layer is not initialized. Call init() method")
            }
            return instance
        }
    }

}


internal fun provideModules(applicationContext: Context,
                            deviceId: String,
                            deviceName: String): List<Module> {
    return listOf(

    )
}