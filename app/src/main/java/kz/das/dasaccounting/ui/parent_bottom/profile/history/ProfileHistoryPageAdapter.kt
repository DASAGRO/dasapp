package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileHistoryPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle,
                                private val historyAcceptedFragment: HistoryAcceptedFragment,
                                private val historyGivenFragment: HistoryGivenFragment): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> historyAcceptedFragment
            1 -> historyGivenFragment
            else -> historyAcceptedFragment
        }
    }

    override fun getItemCount(): Int {
        return ITEM_SIZE
    }

    companion object {
        private const val ITEM_SIZE = 2
    }
}