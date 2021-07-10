package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentWarehouseRolePickBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseTransferPickFragment: BaseFragment<WarehouseTransferPickVM, FragmentWarehouseRolePickBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory) = DasAppScreen(WarehouseTransferPickFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseTransferPickVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseRolePickBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setWarehouseInventory(getWarehouse())

    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

}