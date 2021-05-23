package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfilePassResetBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ProfilePassResetFragment: BaseFragment<ProfilePassResetVM, FragmentProfilePassResetBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfilePassResetFragment())
    }

    override val mViewModel: ProfilePassResetVM by viewModel()

    override fun getViewBinding() = FragmentProfilePassResetBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.run {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }
            btnConfirm.setOnClickListener { requireRouter().navigateTo(ProfilePassResetConfirmFragment.getScreen()) }
        }
    }

}