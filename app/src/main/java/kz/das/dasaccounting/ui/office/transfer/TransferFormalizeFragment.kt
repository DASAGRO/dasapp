package kz.das.dasaccounting.ui.office.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetMakeTransferBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetMakeTransferBinding, TransferFormalizeVM>() {

    companion object {

        private const val OFFICE_INVENTORY = "office_inventory"

        fun newInstance(officeInventory: OfficeInventory) = TransferFormalizeFragment().apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventory)
            this.arguments = args
        }

    }

    private var listener: OnTransferCallback ?= null

    interface OnTransferCallback {
        fun onTransfer(officeInventory: OfficeInventory)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetMakeTransferBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())
        mViewBinding.apply {
            this.btnMakeTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    listener?.onTransfer(it)
                    dismiss()
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.inventory_total_quantity) +
                            " " + it.quantity +
                            " " + it.type)
            }
        })
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}