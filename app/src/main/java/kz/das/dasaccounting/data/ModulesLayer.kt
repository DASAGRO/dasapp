package kz.das.dasaccounting.data

import android.content.Context
import com.orhanobut.hawk.Hawk
import kz.das.dasaccounting.di.getApiModule
import kz.das.dasaccounting.di.getAuthViewModels
import kz.das.dasaccounting.di.getConfigViewModels
import kz.das.dasaccounting.di.getDatabaseModule
import kz.das.dasaccounting.di.getLocationViewModels
import kz.das.dasaccounting.di.getMainViewModels
import kz.das.dasaccounting.di.getModulePreferences
import kz.das.dasaccounting.di.getNetworkModule
import kz.das.dasaccounting.di.getOfficeViewModels
import kz.das.dasaccounting.di.getProfileViewModels
import kz.das.dasaccounting.di.getRepositoriesModule
import org.koin.core.Koin
import org.koin.dsl.koinApplication

class ModulesLayer {

    companion object {
        private lateinit var instance: ModulesLayer

        lateinit var koin: Koin

        fun init(applicationContext: Context) {
            Hawk.init(applicationContext).build()
            koin = koinApplication {
                modules(
                    listOf(getDatabaseModule(applicationContext),
                        getModulePreferences(applicationContext),
                        getNetworkModule(),
                        getApiModule(),
                        getRepositoriesModule(),
                        getConfigViewModels(),
                        getAuthViewModels(),
                        getMainViewModels(),
                        getOfficeViewModels(),
                        getProfileViewModels(),
                        getLocationViewModels())
                )
            }.koin

            instance = ModulesLayer()
        }

        fun getInstance(): ModulesLayer {
            if (!::instance.isInitialized) {
                throw Exception("Data layer is not initialized. Call init() method")
            }
            return instance
        }
    }

}