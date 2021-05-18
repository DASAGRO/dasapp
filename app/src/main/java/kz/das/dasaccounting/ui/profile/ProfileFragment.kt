package kz.das.dasaccounting.ui.profile

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileBinding
import kz.das.dasaccounting.ui.hideBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment: BaseFragment<ProfileVM, FragmentProfileBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileFragment())
    }

    override val mViewModel: ProfileVM by viewModel()

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.clProfile.setOnClickListener {
            hideBottomNavMenu()
            requireRouter().navigateTo(ProfileInfoFragment.getScreen())
        }
    }


}