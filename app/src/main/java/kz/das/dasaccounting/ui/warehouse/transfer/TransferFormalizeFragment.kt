package kz.das.dasaccounting.ui.warehouse.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetMakeTransferBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetMakeTransferBinding, TransferFormalizeVM>() {

    companion object {

        private const val WAREHOUSE = "warehouse"
        fun newInstance(warehouseInventory: WarehouseInventory?) = TransferFormalizeFragment().apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE, warehouseInventory)
            this.arguments = args
        }

    }

    private var listener: OnTransferCallback ?= null

    interface OnTransferCallback {
        fun onTransfer(warehouseInventory: WarehouseInventory)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetMakeTransferBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setWarehouseInventory(getWarehouse())
        mViewBinding.apply {
            this.btnMakeTransfer.setOnClickListener {
                getWarehouse()?.let {
                    it.date = System.currentTimeMillis()
                    it.latitude = mViewModel.getUserLocation().lat
                    it.longitude = mViewModel.getUserLocation().long
                    listener?.onTransfer(it)
                    dismiss()
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

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE)
    }

}