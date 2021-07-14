package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.viewpager2.widget.ViewPager2
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileHistoryFragment: BaseFragment<ProfileHistoryVM, FragmentProfileHistoryBinding>() {

    private var pageAdapter: ViewPager2? = null


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