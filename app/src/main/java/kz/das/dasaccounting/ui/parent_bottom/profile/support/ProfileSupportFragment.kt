package kz.das.dasaccounting.ui.parent_bottom.profile.support

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileSupportBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileSupportFragment: BaseFragment<ProfileSupportVM, FragmentProfileSupportBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileSupportFragment())
    }

    override val mViewModel: ProfileSupportVM by viewModel()

    override fun getViewBinding() = FragmentProfileSupportBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }
    }


    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }

}