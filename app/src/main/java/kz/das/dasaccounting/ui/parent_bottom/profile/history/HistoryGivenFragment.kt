package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    companion object {
        fun newInstance() = HistoryGivenFragment()
    }

    private var historyAdapter: ProfileHistoryAdapter? = null

    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI() {
        historyAdapter = ProfileHistoryAdapter(requireContext(), arrayListOf())
        historyAdapter?.setHistoryOperationsAdapterEvent(object : ProfileHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(title: String?, descr: String?, type: String?, status: String?) {

            }
        })
        mViewBinding.rvGiven.apply {
            this.adapter = historyAdapter
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getHistoryOfficeInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.ACCEPTED.status })
                historyAdapter?.clearOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })
        mViewModel.getHistoryTransportInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.ACCEPTED.status })
                historyAdapter?.clearTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
        mViewModel.getHistoryWarehouseInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.ACCEPTED.status })
                historyAdapter?.clearWarehouseOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.sentTransportInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                historyAdapter?.clearAwaitAcceptedOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.sentOfficeInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                historyAdapter?.clearAwaitAcceptedTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
    }

}