package kz.das.dasaccounting.ui.warehouse.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptInventoryInfoFragment: BaseFragment<AcceptInventoryInfoVM, FragmentInventoryAcceptBinding>() {

    companion object {

        private const val WAREHOUSE = "warehouse"

        fun getScreen(transportInventory: WarehouseInventory) = DasAppScreen(AcceptInventoryInfoFragment())
            .apply {
                val args = Bundle()
                args.putParcelable(WAREHOUSE, transportInventory)
                this.setArgs(args)
            }
    }

    override val mViewModel: AcceptInventoryInfoVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setWarehouseInventory(getWarehouseInventory())

        mViewBinding.apply {
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }

            btnCancel.setOnClickListener {
                requireRouter().exit()
            }

            btnAccept.setOnClickListener {
                getWarehouseInventory()?.let {
                    it.date = System.currentTimeMillis()
                    it.latitude = mViewModel.getUserLocation().lat
                    it.longitude = mViewModel.getUserLocation().long
                    requireRouter().replaceScreen(AcceptConfirmationFragment.getScreen(it))
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getWarehouseInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_warehouse)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.seal_number) +
                            " " + it.sealNumber)
            }
        })
    }

    private fun getWarehouseInventory(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE)
    }

}