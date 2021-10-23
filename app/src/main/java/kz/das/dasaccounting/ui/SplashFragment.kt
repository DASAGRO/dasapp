package kz.das.dasaccounting.ui

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.extensions.animateInfinitePulse
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.databinding.FragmentSplashBinding
import kz.das.dasaccounting.ui.auth.login.LoginFragment
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment: BaseFragment<SplashVM, FragmentSplashBinding>() {

    override val mViewModel: SplashVM by viewModel()

    companion object {
        fun getScreen() = DasAppScreen(SplashFragment())
    }

    override fun getViewBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        //changeStatusColor(R.color.white)
        mViewBinding.ivLogo.animateInfinitePulse(0.5f, 0.5f, 250)
        mViewBinding.tvVersion.text = "Version ${context?.packageManager?.getPackageInfo(context?.packageName!!, 0)!!.versionName}"

        delayedTask(1000, CoroutineScope(Dispatchers.Main)) {
            if (mViewModel.isUserOnSession()) {
                mViewModel.getUserRole()?.let {
                    Screens.getRoleScreens(it)?.let { screen -> requireRouter().newRootScreen(screen) }
                }
            } else {
                requireRouter().newRootScreen(LoginFragment.getScreen())
            }
        }
    }

}