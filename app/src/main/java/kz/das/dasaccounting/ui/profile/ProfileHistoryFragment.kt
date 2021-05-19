package kz.das.dasaccounting.ui.profile

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryBinding
import kz.das.dasaccounting.ui.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileHistoryFragment: BaseFragment<ProfileHistoryVM, FragmentProfileHistoryBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileHistoryFragment())
    }

    override val mViewModel: ProfileHistoryVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }
    }


    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }

}