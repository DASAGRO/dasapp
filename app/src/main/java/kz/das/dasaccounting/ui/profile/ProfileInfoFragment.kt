package kz.das.dasaccounting.ui.profile

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileInfoBinding
import kz.das.dasaccounting.ui.profile.pass_reset.ProfilePassResetFragment
import kz.das.dasaccounting.ui.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileInfoFragment: BaseFragment<ProfileInfoVM, FragmentProfileInfoBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileInfoFragment())
    }

    override val mViewModel: ProfileInfoVM by viewModel()

    override fun getViewBinding() = FragmentProfileInfoBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewBinding.run {
            this.toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            this.rlPassReset.setOnClickListener {
                requireRouter().navigateTo(ProfilePassResetFragment.getScreen())
            }
        }

    }

    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }
}