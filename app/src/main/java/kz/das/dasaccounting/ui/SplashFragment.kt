package kz.das.dasaccounting.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.extensions.animateInfinitePulse
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.databinding.FragmentSplashBinding
import kz.das.dasaccounting.ui.auth.login.LoginFragment
import kz.das.dasaccounting.ui.office.OfficeBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment: BaseFragment<SplashVM, FragmentSplashBinding>() {

    override val mViewModel: SplashVM by viewModel()

    companion object {
        fun getScreen() = DasAppScreen(SplashFragment())
    }

    override fun getViewBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun setupUI() {
        changeStatusColor(R.color.white)
        mViewBinding.ivLogo.animateInfinitePulse(0.5f, 0.5f, 250)
        delayedTask(1000, CoroutineScope(Dispatchers.Main)) {
            if (mViewModel.isUserOnSession()) {
//                mViewModel.getUserRole()?.let {
//                    Screens.getRoleScreens(it)?.let { screen -> requireRouter().newRootScreen(screen) }
//                }
                requireRouter().newRootScreen(OfficeBottomNavigationFragment.getScreen())
            } else {
                requireRouter().newRootScreen(LoginFragment.getScreen())
            }
        }
    }

}