package kz.das.dasaccounting.ui.auth.password_reset

import android.os.Bundle
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordResetBinding
import kz.das.dasaccounting.domain.data.Profile
import androidx.lifecycle.Observer
import kz.das.dasaccounting.ui.Screens
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PassResetFragment: BaseFragment<PassResetVM, FragmentPasswordResetBinding>() {

    companion object {
        private const val PROFILE = "profile"

        fun getScreen(profile: Profile) = DasAppScreen(PassResetFragment()).apply {
            val args = Bundle()
            args.putParcelable(PROFILE, profile)
            this.setArgs(args)
        }
    }

    override val mViewModel: PassResetVM by viewModel{ parametersOf(getProfile()) }

    override fun getViewBinding() = FragmentPasswordResetBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            btnConfirm.setOnClickListener {
                mViewModel.login(mViewBinding.edtPassword.text.toString())
            }
            tvRemaining.setOnClickListener {
                mViewModel.restartTime()
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.run {
            isLoginConfirmed().observe(viewLifecycleOwner, Observer {
                if (it) {
                    mViewModel.getUserRole()?.let { userRole ->
                        Screens.getRoleScreens(userRole)?.let { screen -> requireRouter().newRootScreen(screen) }
                    }
                } else {
                    showError(getString(R.string.common_error), getString(R.string.error_incorrect_password))
                }
            })
            isTimerFinished().observe(viewLifecycleOwner, Observer {
                if (it) {
                    mViewBinding.tvRemaining.text = getString(R.string.enter_password_retry)
                }
            })
            getRemainedTime().observe(viewLifecycleOwner, Observer {
                mViewBinding.tvRemaining.text = String.format(getString(R.string.enter_password_retry_remaining), it)
            })
        }
    }

    private fun getProfile(): Profile? {
        return arguments?.getParcelable(PassResetFragment.PROFILE)
    }

}