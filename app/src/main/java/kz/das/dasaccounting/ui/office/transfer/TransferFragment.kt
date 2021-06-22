package kz.das.dasaccounting.ui.office.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import co.infinum.goldfinger.Goldfinger
import co.infinum.goldfinger.Goldfinger.PromptParams
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetInventoryInputBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFragment: BaseBottomSheetFragment<FragmentBottomSheetInventoryInputBinding, TransferVM>() {

    companion object {

        private const val OFFICE_INVENTORY = "office_inventory"

        fun newInstance(officeInventory: OfficeInventory) = TransferFragment().apply {
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

    override val mViewModel: TransferVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetInventoryInputBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())
        mViewBinding.apply {
            this.ivClose.setOnClickListener {
                dismiss()
            }
            this.btnTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    if (it.quantity ?: 0 >= mViewBinding.edtQuantity.text.toString().toInt()) {
                        it.quantity = mViewBinding.edtQuantity.text.toString().toInt()
                        checkConfirmation(it)
                    } else {
                        showError(getString(R.string.common_error), getString(R.string.quantity_compare_error))
                    }
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.tvNaming.text = it.name
                mViewBinding.tvQuantity.text =
                    (String.format(getString(R.string.total_quantity), it.quantity.toString()) + " " + it.type)
            }
        })
    }

    private fun checkConfirmation(officeInventory: OfficeInventory) {
        val goldfinger = Goldfinger.Builder(requireContext()).build()
        if (goldfinger.canAuthenticate()) {
            val params = PromptParams.Builder(requireActivity())
                .title(getString(R.string.confirm_with_finger))
                .negativeButtonText(getString(R.string.cancel))
                .description("")
                .subtitle("")
                .build()
            goldfinger.authenticate(params, object : Goldfinger.Callback {
                override fun onError(e: Exception) {
                    showError(getString(R.string.common_error), getString(R.string.common_unexpected_error))
                }

                override fun onResult(result: Goldfinger.Result) {
                    if (result.reason() == Goldfinger.Reason.AUTHENTICATION_SUCCESS) {
                        listener?.onTransfer(officeInventory)
                        dismiss()
                    } else if (result.reason() == Goldfinger.Reason.AUTHENTICATION_FAIL) {
                        showError(getString(R.string.common_error), getString(R.string.error_not_valid_finger))
                    }
                 }
            })
        } else {
            listener?.onTransfer(officeInventory)
            dismiss()
        }
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}