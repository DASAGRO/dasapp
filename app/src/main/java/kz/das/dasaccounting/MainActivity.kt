package kz.das.dasaccounting

import android.content.pm.ActivityInfo
import android.os.Bundle
import kz.das.dasaccounting.core.navigation.RouterProvider
import kz.das.dasaccounting.core.navigation.ScreenNavigator
import kz.das.dasaccounting.core.navigation.onBackPressed
import kz.das.dasaccounting.core.ui.activities.BaseActivity
import kz.das.dasaccounting.databinding.ActivityMainBinding
import kz.das.dasaccounting.ui.SplashFragment
import org.koin.android.viewmodel.ext.android.viewModel
import ru.terrakok.cicerone.Cicerone

class MainActivity: BaseActivity<MainVM, ActivityMainBinding>(), RouterProvider {

    private val cicerone = Cicerone.create()
    private val appNavigator by lazy {
        ScreenNavigator(this, R.id.container)
    }

    override val mViewModel: MainVM by viewModel()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(appNavigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        val view = mViewBinding.root
        setContentView(view)
        getRouter().newRootScreen(SplashFragment.getScreen())
    }

    override fun restartActivity() {
        super.restartActivity()
    }

    override fun onLogout() {
        super.onLogout()
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.onBackPressed()) {
            getRouter().exit()
        }
    }

    override fun getRouter() = cicerone.router

}