package kz.das.dasaccounting.ui.drivers.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetMakeTransferBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetMakeTransferBinding, TransferFormalizeVM>() {

    companion object {

        private const val TRANSPORT_INVENTORY = "inventory"

        fun newInstance(transportInventory: TransportInventory) = TransferFormalizeFragment().apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, transportInventory)
            this.arguments = args
        }

    }

    private var listener: OnTransferCallback ?= null

    interface OnTransferCallback {
        fun onTransfer(transportInventory: TransportInventory)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetMakeTransferBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getOfficeInventory())
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
        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.model
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.gov_number) +
                            " " + it.stateNumber)
            }
        })
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun getOfficeInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}