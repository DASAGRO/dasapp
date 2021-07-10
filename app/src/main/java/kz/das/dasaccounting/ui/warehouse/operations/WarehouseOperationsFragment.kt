package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentSearchBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseOperationsFragment: BaseFragment<WarehouseOperationsVM, FragmentSearchBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory) = DasAppScreen(WarehouseOperationsFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseOperationsVM by viewModel()

    override fun getViewBinding() = FragmentSearchBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setWarehouseInventory(getWarehouse())
    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }
}