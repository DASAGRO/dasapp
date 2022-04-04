package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryAcceptedBinding
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryAcceptedFragment: BaseFragment<HistoryAcceptedVM, FragmentProfileHistoryAcceptedBinding>() {

    companion object {
        fun newInstance() = HistoryAcceptedFragment()
    }

    private var historyAdapter: UserTransferHistoryAdapter? = null

    private val historyViewModel: ProfileHistoryVM by sharedViewModel()
    override val mViewModel: HistoryAcceptedVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryAcceptedBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        historyAdapter = UserTransferHistoryAdapter(requireContext(), arrayListOf())
        historyAdapter?.setHistoryOperationsAdapterEvent(object : UserTransferHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(title: String?, descr: String?, type: String?, status: String?, qr: String?, transferType: String?) {
                requireRouter().navigateTo(HistoryDetailFragment.getScreen(title, descr, type, status, mViewModel.getQrData(qr, transferType, status)))
            }
        })
        mViewBinding.rvAccepted.apply {
            this.adapter = historyAdapter
        }
        observeHistory()
    }

    override fun observeLiveData() {
        super.observeLiveData()

        historyViewModel.isNeedRefresh.observe(viewLifecycleOwner, {
            if(it){
                observeHistory()
            }
        })
    }

    private fun observeHistory() {
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
            val distinctList = historyList.distinct()
            val sorted = distinctList.sortedByDescending { it.date }
            historyAdapter?.putItems(sorted)
        })
    }
}
