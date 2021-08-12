package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentWarehouseTranferActionBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseOptionsFragment: BaseFragment<WarehouseOptionsVM, FragmentWarehouseTranferActionBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory?) = DasAppScreen(WarehouseOptionsFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseOptionsVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseTranferActionBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setWarehouseInventory(getWarehouse())

        mViewBinding.apply {
            tvWarehouseActionsTitle.text = getWarehouse()?.name
            llTransferWarehouse.setOnClickListener {
                requireRouter().navigateTo(WarehouseTransferPickFragment.getScreen(getWarehouse()))
            }
            llTransferInventory.setOnClickListener {
                requireRouter().navigateTo(WarehouseOperationsFragment.getScreen(getWarehouse()))
            }
        }

    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

}