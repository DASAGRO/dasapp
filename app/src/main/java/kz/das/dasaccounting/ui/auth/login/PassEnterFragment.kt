package kz.das.dasaccounting.ui.auth.login

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordBinding
import kz.das.dasaccounting.ui.ParentBottomNavigationFragment
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetFragment
import org.koin.android.viewmodel.ext.android.viewModel

class PassEnterFragment: BaseFragment<PassEnterVM, FragmentPasswordBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(PassEnterFragment())
    }

    override val mViewModel: PassEnterVM by viewModel()

    override fun getViewBinding() = FragmentPasswordBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewBinding.tvPasswordReset.setOnClickListener {
            requireRouter().navigateTo(PhonePassResetFragment.getScreen())
        }

        mViewBinding.btnConfirm.setOnClickListener {
            requireRouter().navigateTo(ParentBottomNavigationFragment.getScreen())
        }

    }

}