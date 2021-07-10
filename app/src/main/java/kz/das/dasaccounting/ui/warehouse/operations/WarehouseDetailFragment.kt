package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentWarehouseActionsBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseDetailFragment: BaseFragment<WarehouseDetailVM, FragmentWarehouseActionsBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory) = DasAppScreen(WarehouseDetailFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseDetailVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseActionsBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setWarehouseInventory(getWarehouse())
        mViewBinding.apply {
            tvWarehouseActionsTitle.text = mViewModel.getWarehouseInventory()?.name
            llActionList.setOnClickListener {

            }
            llActionSwap.setOnClickListener {

            }
            llActionAdd.setOnClickListener {
                showQrDialog()
            }
        }
    }

    private fun showQrDialog() {

    }

    private fun handleResult() {

    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

}