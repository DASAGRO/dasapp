package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfilePassResetConfirmBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ProfilePassResetConfirmFragment: BaseFragment<ProfilePassResetVM, FragmentProfilePassResetConfirmBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfilePassResetConfirmFragment())
    }

    override val mViewModel: ProfilePassResetVM by viewModel()

    override fun getViewBinding() = FragmentProfilePassResetConfirmBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.run {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }

        }
    }


}