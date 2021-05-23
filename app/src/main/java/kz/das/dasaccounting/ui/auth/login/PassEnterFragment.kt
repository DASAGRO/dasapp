package kz.das.dasaccounting.ui.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordBinding
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.ui.parent_bottom.ParentBottomNavigationFragment
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PassEnterFragment: BaseFragment<PassEnterVM, FragmentPasswordBinding>() {

    companion object {
        private const val PROFILE = "profile"

        fun getScreen(profile: Profile) = DasAppScreen(newInstance(profile))

        private fun newInstance(profile: Profile): Fragment {
            val args = Bundle()
            args.putParcelable(PROFILE, profile)

            val fragment = PassEnterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: PassEnterVM by viewModel { parametersOf(getProfile()) }

    override fun getViewBinding() = FragmentPasswordBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewBinding.tvPasswordReset.setOnClickListener {
            requireRouter().navigateTo(PhonePassResetFragment.getScreen())
        }

        mViewBinding.btnConfirm.setOnClickListener {
            mViewModel.login(mViewBinding.edtPassword.text.toString())
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isLoginConfirmed().observe(viewLifecycleOwner, Observer {
            if (it) {
                requireRouter().navigateTo(ParentBottomNavigationFragment.getScreen())
            }
        })
    }

    private fun getProfile(): Profile? {
        return arguments?.getParcelable(PROFILE)
    }

}