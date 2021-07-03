package kz.das.dasaccounting.ui.drivers.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.core.ui.extensions.verifyToInit
import kz.das.dasaccounting.databinding.FragmentBottomSheetMakeTransferBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetMakeTransferBinding, kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeVM>() {

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

    override val mViewModel: kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetMakeTransferBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getOfficeInventory())
        mViewBinding.apply {
            this.btnMakeTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    checkConfirmation(it)
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setTsTypeImage(it)
                mViewBinding.tvInventoryTitle.text = it.model
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.gov_number) +
                            " " + it.stateNumber)
            }
        })
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun checkConfirmation(transport: TransportInventory) {
        this@TransferFormalizeFragment.verifyToInit(
            {
                listener?.onTransfer(transport)
                dismiss()
            },
            { showError(getString(R.string.common_error), getString(R.string.error_not_valid_finger)) },
            { showError(getString(R.string.common_error), getString(R.string.common_unexpected_error)) }
        )
    }

    private fun getOfficeInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}