package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentSearchBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFragment
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseOperationsFragment: BaseFragment<WarehouseOperationsVM, FragmentSearchBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory?) = DasAppScreen(WarehouseOperationsFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    private var warehouseOperationsAdapter: WarehouseOperationsAdapter ?= null

    override val mViewModel: WarehouseOperationsVM by viewModel()

    override fun getViewBinding() = FragmentSearchBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        warehouseOperationsAdapter = WarehouseOperationsAdapter(requireContext(), arrayListOf())
        warehouseOperationsAdapter?.setWarehouseOperationsAdapterEvent(object : WarehouseOperationsAdapter.OnWarehouseOperationsAdapterEvent {
            override fun onOfficeInventory(officeInventory: OfficeInventory) {
                officeInventory.storeUUIDSender = mViewModel.getWarehouseInventory()?.storeUUID
                showTransferDialog(officeInventory)
            }

            override fun onTransportInventory(transportInventory: TransportInventory) {
                transportInventory.storeUUIDSender = mViewModel.getWarehouseInventory()?.storeUUID
                showTransportTransferDialog(transportInventory)
            }
        })

        mViewModel.setWarehouseInventory(getWarehouse())
        mViewModel.retrieve()
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }
            rvItems.adapter = warehouseOperationsAdapter
        }
    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getInventories().observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                warehouseOperationsAdapter?.putItems(it)
                applySearchEnable()
            }
        })
    }

    private fun applySearchEnable() {
        mViewBinding.edtSearch.addTextChangedListener { searchName ->
            if (searchName.isNullOrEmpty()) {
                warehouseOperationsAdapter?.putItems(mViewModel.getInventoryList())
            } else {
                warehouseOperationsAdapter?.putItems(mViewModel.getInventoryList().filter { operationAct ->
                    (operationAct is OfficeInventory && operationAct.name?.contains(searchName.toString().trim(), ignoreCase = true) == true) ||
                            (operationAct is TransportInventory && operationAct.model.contains(searchName.toString().trim(), ignoreCase = true))
                })
            }
        }
    }

    private fun showTransferDialog(officeInventory: OfficeInventory) {
        val transferFragment = TransferFragment.newInstance(officeInventory)
        transferFragment.setOnTransferCallback(object : TransferFragment.OnTransferCallback {
            override fun onTransfer(officeInventory: OfficeInventory) {
                requireRouter().navigateTo(TransferConfirmFragment.getScreen(officeInventory))
            }
        })
        transferFragment.show(childFragmentManager, transferFragment.tag)
    }

    private fun showTransportTransferDialog(transportInventory: TransportInventory) {
        val transferFragment = kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.newInstance(transportInventory)
        transferFragment.setOnTransferCallback(object : kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.OnTransferCallback {
            override fun onTransfer(transportInventory: TransportInventory) {
                requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmFragment.getScreen(transportInventory))
            }
        })
        transferFragment.show(childFragmentManager, transferFragment.tag)
    }

}