package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.verifyToInit
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentWarehouseRolePickBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.warehouse.transfer.TransferAdditionalFragment
import kz.das.dasaccounting.ui.warehouse.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.warehouse.transfer.TransferFormalizeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseTransferPickFragment: BaseFragment<WarehouseTransferPickVM, FragmentWarehouseRolePickBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory?) = DasAppScreen(WarehouseTransferPickFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseTransferPickVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseRolePickBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setWarehouseInventory(getWarehouse())

        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }

        mViewBinding.btnSecurity.setOnClickListener {
            showSecurityTransferDialog()
        }
        mViewBinding.btnWarehouseHead.setOnClickListener {
            showWarehouseTransferDialog()
        }
    }

    private fun showSecurityTransferDialog() {
        val showSecurityTransferDialog = TransferAdditionalFragment.newInstance(getWarehouse())
        showSecurityTransferDialog.setOnTransferFieldsListener(object: TransferAdditionalFragment.OnTransferFieldsListener {
            override fun onTransfer(warehouse: WarehouseInventory?, fileIds: ArrayList<Int>) {
                this@WarehouseTransferPickFragment.verifyToInit(
                    {
                        requireRouter().replaceScreen(TransferConfirmFragment.getScreen(warehouse, fileIds))
                    },
                    { showError(getString(R.string.common_error), getString(R.string.error_not_valid_finger)) },
                    { showError(getString(R.string.common_error), getString(R.string.common_unexpected_error)) }
                )
            }
        })
        showSecurityTransferDialog.show(childFragmentManager, "TransferAdditionalFragment")
    }

    private fun showWarehouseTransferDialog() {
        val showTransferDialog = TransferFormalizeFragment.newInstance(getWarehouse())
        showTransferDialog.setOnTransferCallback(object : TransferFormalizeFragment.OnTransferCallback {
            override fun onTransfer(warehouseInventory: WarehouseInventory) {
                this@WarehouseTransferPickFragment.verifyToInit(
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