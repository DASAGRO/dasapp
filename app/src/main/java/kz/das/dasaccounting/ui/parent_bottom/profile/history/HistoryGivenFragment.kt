package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    companion object {
        fun newInstance() = HistoryGivenFragment()
    }

    private var historyAdapter: UserTransferHistoryAdapter? = null

    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        historyAdapter = UserTransferHistoryAdapter(requireContext(), arrayListOf(), mViewModel.getUser()?.firstName +  " " + mViewModel.getUser()?.lastName)
        historyAdapter?.setHistoryOperationsAdapterEvent(object : UserTransferHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(title: String?, descr: String?, type: String?, status: String?) {
                requireRouter().navigateTo(HistoryDetailFragment.getScreen(title, descr, type, status))
            }
        })
        mViewBinding.rvGiven.apply {
            this.adapter = historyAdapter
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getZippedHistory().observe(viewLifecycleOwner, Observer { list ->
            val historyList = arrayListOf<HistoryTransfer>()
            list.forEach {
                if (it is ArrayList<*>) {
                    it.forEach { item ->
                        if (item is HistoryTransfer) {
                            historyList.add(item)
                        }
                    }
                }
            }
            val sorted = historyList.sortedByDescending { it.date }
            historyAdapter?.putItems(sorted)
        })

    }

}