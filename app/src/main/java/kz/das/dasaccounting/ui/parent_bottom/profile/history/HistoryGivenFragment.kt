package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.drivers.toSent
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.office.toSent
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    companion object {
        fun newInstance() = HistoryGivenFragment()
    }

    private var historyAdapter: ProfileHistoryAdapter? = null

    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        historyAdapter = ProfileHistoryAdapter(requireContext(), arrayListOf(), mViewModel.getUser()?.firstName + mViewModel.getUser()?.lastName)
        historyAdapter?.setHistoryOperationsAdapterEvent(object : ProfileHistoryAdapter.OnHistoryOperationsAdapterEvent {
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
        mViewModel.getHistoryOfficeInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.SENT.status })
                historyAdapter?.clearOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })
        mViewModel.getHistoryTransportInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.SENT.status })
                historyAdapter?.clearTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
        mViewModel.getHistoryWarehouseInventoriesLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                acceptedList.addAll(list.filter { item -> item.status == HistoryEnum.SENT.status })
                historyAdapter?.clearWarehouseOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.sentTransportInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                val newList = list.map { officeInventory -> officeInventory.toSent() }
                acceptedList.addAll(newList)
                historyAdapter?.clearAwaitAcceptedOperations()
                historyAdapter?.addAll(acceptedList)
            }
        })

        mViewModel.sentOfficeInventoryLocally().observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                val acceptedList: ArrayList<OperationAct> = arrayListOf()
                val newList = list.map { transportInventory -> transportInventory.toSent() }
                acceptedList.addAll(newList)
                historyAdapter?.clearAwaitAcceptedTransports()
                historyAdapter?.addAll(acceptedList)
            }
        })
    }

}