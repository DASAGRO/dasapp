package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileHistoryFragment: BaseFragment<ProfileHistoryVM, FragmentProfileHistoryBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileHistoryFragment())
    }

    override val mViewModel: ProfileHistoryVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }

        mViewBinding.vpHistory.run {
                isSaveEnabled = true
                isUserInputEnabled = false
                adapter = ProfileHistoryPageAdapter(childFragmentManager, lifecycle,
                    HistoryAcceptedFragment.newInstance(), HistoryGivenFragment.newInstance())
                offscreenPageLimit = 2
                TabLayoutMediator(mViewBinding.tlHistory, mViewBinding.vpHistory) {
                        tab, position ->
                    tab.text = if (position == 0) getString(R.string.accept_title) else getString(R.string.give_title)
                }.attach()
            }


    }


    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }

}