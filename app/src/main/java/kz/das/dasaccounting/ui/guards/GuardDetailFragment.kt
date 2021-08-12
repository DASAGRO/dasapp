package kz.das.dasaccounting.ui.guards

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.verifyToInit
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentWarehouseActionsBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseDetailVM
import kz.das.dasaccounting.ui.warehouse.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.warehouse.transfer.TransferFormalizeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class GuardDetailFragment: BaseFragment<WarehouseDetailVM, FragmentWarehouseActionsBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory?) = DasAppScreen(
            GuardDetailFragment()
        ).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseDetailVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseActionsBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setWarehouseInventory(getWarehouse())
        mViewBinding.apply {
            tvWarehouseActionsTitle.text = mViewModel.getWarehouseInventory()?.name
            this.llActionList.visibility = View.GONE
            this.llActionAdd.visibility = View.GONE
            tvNameSwap.text = "Передать склад"
            llActionSwap.setOnClickListener {
                showTransferDialog()
            }
        }
    }

    private fun showTransferDialog() {
        val showTransferDialog = TransferFormalizeFragment.newInstance(getWarehouse())
        showTransferDialog.setOnTransferCallback(object : TransferFormalizeFragment.OnTransferCallback {
            override fun onTransfer(warehouseInventory: WarehouseInventory) {
                this@GuardDetailFragment.verifyToInit(
                    {
                        requireRouter().replaceScreen(TransferConfirmFragment.getScreen(warehouseInventory, arrayListOf()))
                    },
                    { showError(getString(R.string.common_error), getString(R.string.error_not_valid_finger)) },
                    { showError(getString(R.string.common_error), getString(R.string.common_unexpected_error)) }
                )
            }
        })
        showTransferDialog.show(childFragmentManager, "TransferFormalizeFragment")
    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

}