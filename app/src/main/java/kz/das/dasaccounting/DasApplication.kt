package kz.das.dasaccounting


import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import kz.das.dasaccounting.di.getAuthViewModels
import kz.das.dasaccounting.di.getConfigViewModels
import kz.das.dasaccounting.di.getLocationViewModels
import kz.das.dasaccounting.di.getMainViewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DasApplication: Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setupKoin()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackgrounded() {
        //Foreground.onBackgrounded()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForegrounded() {
        //Foreground.onForegrounded()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DasApplication)
            modules(
                getConfigViewModels(),
                getAuthViewModels(),
                getMainViewModels(),
                getLocationViewModels()
            )
        }
    }

}
