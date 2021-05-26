package kz.das.dasaccounting.ui.auth.password_reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordResetBinding
import kz.das.dasaccounting.domain.data.Profile
import androidx.lifecycle.Observer
import kz.das.dasaccounting.ui.parent_bottom.ParentBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class PassResetFragment: BaseFragment<PassResetVM, FragmentPasswordResetBinding>() {

    companion object {
        private const val PROFILE = "profile"

        fun getScreen(profile: Profile) = DasAppScreen(newInstance(profile))

        private fun newInstance(profile: Profile): Fragment {
            val args = Bundle()
            args.putParcelable(PROFILE, profile)

            val fragment = PassResetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: PassResetVM by viewModel()

    override fun getViewBinding() = FragmentPasswordResetBinding.inflate(layoutInflater)

    override fun setupUI() {
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
                    requireRouter().navigateTo(ParentBottomNavigationFragment.getScreen())
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