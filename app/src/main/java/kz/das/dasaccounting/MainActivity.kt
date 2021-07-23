package kz.das.dasaccounting

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mapbox.mapboxsdk.Mapbox
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        val view = mViewBinding.root
        setContentView(view)
        observeLiveData()
        getRouter().newRootScreen(SplashFragment.getScreen())
    }

    private fun observeLiveData() {
        mViewModel.isLogoutCompleted().observe(this, Observer {
            if (it) { restartActivity() }
        })
    }

    override fun onLogout() {
        super.onLogout()
        mViewModel.logOut()
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.onBackPressed()) {
            getRouter().exit()
        }
    }

    override fun getRouter() = cicerone.router

//    override fun showLoading() {
//        mViewBinding.progressBar.isVisible = true
//    }
//
//    override fun hideLoading() {
//        mViewBinding.progressBar.isVisible = false
//    }
//
//    override fun showUploading() {
//        mViewBinding.progressBar.isVisible = true
//    }
//
//    override fun hideUploading() {
//        mViewBinding.progressBar.isVisible = false
//    }

}