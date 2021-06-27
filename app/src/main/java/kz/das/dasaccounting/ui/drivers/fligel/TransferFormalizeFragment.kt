package kz.das.dasaccounting.ui.drivers.fligel

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetGatherMakeTranferBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFligelDataFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetGatherMakeTranferBinding, TransferFligeDataFormalizeVM>() {

    companion object {

        private const val TRANSPORT_INVENTORY = "inventory"

        fun newInstance(transportInventory: TransportInventory) = TransferFligelDataFormalizeFragment().apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, transportInventory)
            this.arguments = args
        }
    }

    private var listener: OnTransferCallback ?= null

    interface OnTransferCallback {
        fun onTransfer(transportInventory: TransportInventory)
        fun onAccept(transportInventory: TransportInventory)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferFligeDataFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetGatherMakeTranferBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getOfficeInventory())
        mViewBinding.apply {
            this.btnMakeTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    listener?.onTransfer(it)
                    dismiss()
                }
            }
            this.btnMakeTransferAccept.setOnClickListener {
                getOfficeInventory()?.let {
                    listener?.onAccept(it)
                    dismiss()
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

    private fun getOfficeInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}