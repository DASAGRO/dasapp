package kz.das.dasaccounting.di

import android.content.Context
import kz.das.dasaccounting.data.source.preferences.UserPreferences
import org.koin.dsl.module

const val SHARED_PREFERENCES_NAME = "private_das_app_preferences"

internal fun getModulePreferences(applicationContext: Context) = module {
    single { provideUserPreferences(applicationContext) }
}

private fun provideUserPreferences(applicationContext: Context): UserPreferences {
    return UserPreferences(applicationContext.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    ))
}