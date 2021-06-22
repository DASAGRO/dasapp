package kz.das.dasaccounting.di

import android.content.Context
import kz.das.dasaccounting.data.source.local.DasAppDatabase
import org.koin.dsl.module

internal fun getDatabaseModule(context: Context) = module {
    single { provideDatabase(context) }
}

fun provideDatabase(androidContext: Context): DasAppDatabase? {
    return DasAppDatabase.getInstance(androidContext)
}
