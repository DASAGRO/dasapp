package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryAcceptedBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryAcceptedFragment: BaseFragment<HistoryAcceptedVM, FragmentProfileHistoryAcceptedBinding>() {

    companion object {
        fun newInstance() = HistoryAcceptedFragment()
    }

    private var historyAdapter: ProfileHistoryAdapter? = null

    override val mViewModel: HistoryAcceptedVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryAcceptedBinding.inflate(layoutInflater)

    override fun setupUI() {
        historyAdapter = ProfileHistoryAdapter(requireContext(), arrayListOf())
        historyAdapter?.setHistoryOperationsAdapterEvent(object : ProfileHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(title: String?, descr: String?, type: String?, status: String?) {

            }
        })

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

        mViewModel.acceptedOfficeInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                historyAdapter?.clearAwaitAcceptedTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.acceptedTransportInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                historyAdapter?.clearAwaitAcceptedTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
    }
}
