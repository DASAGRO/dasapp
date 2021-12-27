package kz.das.dasaccounting.ui.auth.login

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordBinding
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.auth.onboarding.OnBoardingFragment
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.parameter.parametersOf

class PassEnterFragment: BaseFragment<PassEnterVM, FragmentPasswordBinding>(), KoinComponent {

    companion object {
        private const val PROFILE = "profile"

        fun getScreen(profile: Profile): DasAppScreen = DasAppScreen(PassEnterFragment()).apply {
            val arguments = Bundle()
            arguments.putParcelable(PROFILE, profile)
            setArgs(arguments)
        }
    }

    override val mViewModel: PassEnterVM by viewModel { parametersOf(getProfile()) }

    override fun getViewBinding() = FragmentPasswordBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel

            tvPasswordReset.setOnClickListener {
                requireRouter().navigateTo(PhonePassResetFragment.getScreen())
            }
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
            isOnBoardingConfirmed().observe(viewLifecycleOwner, Observer {
                if (it) {
                    requireRouter().newRootScreen(OnBoardingFragment.getScreen())
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
            numberAttemptsLV().observe(viewLifecycleOwner, { it?.let {
                if (it <= 5)
                    mViewModel.login(mViewBinding.edtPassword.text.toString())
                else
                    showError(
                        getString(R.string.common_error),
                        getString(R.string.common_error_could_not_connect_to_server)
                    )
            }})
        }
    }

    private fun getProfile(): Profile {
        return arguments?.getParcelable(PROFILE)
            ?: throw IllegalArgumentException("Profile must not be null")

    }

}