package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.animateRepeatPulse
import kz.das.dasaccounting.core.ui.extensions.rotateAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileHistoryFragment: BaseFragment<ProfileHistoryVM, FragmentProfileHistoryBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileHistoryFragment())
    }

    override val mViewModel: ProfileHistoryVM by viewModel()

    private var hAdapter: ProfileHistoryPageAdapter? = null

    override fun getViewBinding() = FragmentProfileHistoryBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {

        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }

            ivRefresh.setOnClickListener {
                mViewBinding.ivRefresh.animateRepeatPulse()
                mViewBinding.ivRefresh.rotateAnimation()
                mViewModel.setRefresh(true)

                mViewModel.retrieve()

                val curr = vpHistory.currentItem
                vpHistory.adapter = hAdapter
                vpHistory.currentItem = curr
            }
            hAdapter = ProfileHistoryPageAdapter(childFragmentManager, lifecycle,
                HistoryAcceptedFragment.newInstance(), HistoryGivenFragment.newInstance())

            vpHistory.run {
                isSaveEnabled = true
                isUserInputEnabled = false
                adapter = hAdapter
                offscreenPageLimit = 2
                TabLayoutMediator(mViewBinding.tlHistory, mViewBinding.vpHistory) {
                    tab, position ->
                    tab.text = if (position == 0) getString(R.string.accept_title) else getString(R.string.give_title)
                }.attach()
            }
        }
    }



    override fun onDestroy() {
        showBottomNavMenu()
        super.onDestroy()
    }
}