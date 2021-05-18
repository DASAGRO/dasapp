package kz.das.dasaccounting.ui.profile

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileInfoBinding
import kz.das.dasaccounting.ui.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileInfoFragment: BaseFragment<ProfileInfoVM, FragmentProfileInfoBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileInfoFragment())
    }

    override val mViewModel: ProfileInfoVM by viewModel()

    override fun getViewBinding() = FragmentProfileInfoBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.toolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
    }

    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }
}