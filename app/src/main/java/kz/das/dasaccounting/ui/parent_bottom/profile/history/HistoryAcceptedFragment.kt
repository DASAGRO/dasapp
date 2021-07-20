package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryAcceptedBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.drivers.toAccepted
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.office.toAccepted
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryAcceptedFragment: BaseFragment<HistoryAcceptedVM, FragmentProfileHistoryAcceptedBinding>() {

    companion object {
        fun newInstance() = HistoryAcceptedFragment()
    }

    private var historyAdapter: ProfileHistoryAdapter? = null

    override val mViewModel: HistoryAcceptedVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryAcceptedBinding.inflate(layoutInflater)

    override fun setupUI() {
        historyAdapter = ProfileHistoryAdapter(requireContext(), arrayListOf(), mViewModel.getUser()?.firstName + mViewModel.getUser()?.lastName)
        historyAdapter?.setHistoryOperationsAdapterEvent(object : ProfileHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(title: String?, descr: String?, type: String?, status: String?) {
                requireRouter().navigateTo(HistoryDetailFragment.getScreen(title, descr, type, status))
            }
        })
        mViewBinding.rvAccepted.apply {
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

        mViewModel.acceptedOfficeInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val newList = list.map { officeInventory -> officeInventory.toAccepted() }
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(newList)
                historyAdapter?.clearAwaitAcceptedOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.acceptedTransportInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val newList = list.map { transportInventory -> transportInventory.toAccepted() }
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(newList)
                historyAdapter?.clearAwaitAcceptedTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
    }
}
