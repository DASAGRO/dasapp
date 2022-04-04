package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.gson.Gson
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    companion object {
        fun newInstance() = HistoryGivenFragment()
    }

    private var historyAdapter: UserTransferHistoryAdapter? = null

    private val historyViewModel: ProfileHistoryVM by sharedViewModel()
    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        historyAdapter = UserTransferHistoryAdapter(requireContext(), arrayListOf())
        historyAdapter?.setHistoryOperationsAdapterEvent(object :
            UserTransferHistoryAdapter.OnHistoryOperationsAdapterEvent {
            override fun onClick(
                title: String?,
                descr: String?,
                type: String?,
                status: String?,
                qr: String?,
                trasferType: String?
            ) {
                if (status != HistoryEnum.UNFINISHED.status) {
                    requireRouter().navigateTo(
                        HistoryDetailFragment.getScreen(
                            title,
                            descr,
                            type,
                            status,
                            qr ?: ""
                        )
                    )
                } else {
                    val savedInventory = mViewModel.getSavedInventory()!!
                    when (savedInventory.type) {
                        InventoryType.OFFICE -> {
                            val officeInventory =
                                Gson().fromJson(savedInventory.body, OfficeInventory::class.java)
                            requireRouter().navigateTo(
                                TransferConfirmFragment.getScreen(officeInventory)
                            )
                        }

                        InventoryType.TRANSPORT -> {
                            val transportInventory =
                                Gson().fromJson(savedInventory.body, TransportInventory::class.java)
                            requireRouter().navigateTo(
                                kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmFragment.getScreen(
                                    transportInventory
                                )
                            )
                        }

                        InventoryType.WAREHOUSE -> {
                            val warehouseInventory =
                                Gson().fromJson(savedInventory.body, WarehouseInventory::class.java)
                            requireRouter().replaceScreen(
                                kz.das.dasaccounting.ui.warehouse.transfer.TransferConfirmFragment.getScreen(
                                    warehouseInventory,
                                    arrayListOf()
                                )
                            )
                        }
                    }
                }
            }
        })
        mViewBinding.rvGiven.apply {
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
            mViewModel.getSavedInventoryHistory()?.let{ historyList.add(0, it) }
            val distinctList = historyList.distinct()
            val sorted = distinctList.sortedByDescending { it.date }
            historyAdapter?.putItems(sorted)
        })
    }
}