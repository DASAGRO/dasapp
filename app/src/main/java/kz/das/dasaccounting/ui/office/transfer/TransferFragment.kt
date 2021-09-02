package kz.das.dasaccounting.ui.office.transfer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.core.ui.extensions.verifyToInit
import kz.das.dasaccounting.databinding.FragmentBottomSheetInventoryInputBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFragment :
    BaseBottomSheetFragment<FragmentBottomSheetInventoryInputBinding, TransferVM>() {

    companion object {

        private const val OFFICE_INVENTORY = "office_inventory"

        fun newInstance(officeInventory: OfficeInventory) = TransferFragment().apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventory)
            this.arguments = args
        }

    }

    private var listener: OnTransferCallback? = null

    interface OnTransferCallback {
        fun onTransfer(officeInventory: OfficeInventory)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetInventoryInputBinding.inflate(layoutInflater)

    @SuppressLint("ClickableViewAccessibility")
    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        mViewBinding.apply {
            this.ivClose.setOnClickListener {
                dismiss()
            }
            this.btnTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    if (it.quantity ?: 0.0 >= mViewBinding.edtQuantity.text.toString().toDouble()) {
                        it.quantity = mViewBinding.edtQuantity.text.toString().toDouble()
                        checkConfirmation(it)
                    } else {
                        showError(
                            getString(R.string.common_error),
                            getString(R.string.quantity_compare_error)
                        )
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
                    (String.format(
                        getString(R.string.total_quantity),
                        it.quantity.toString()
                    ) + " " + it.type)
            }
        })
    }

    private fun checkConfirmation(officeInventory: OfficeInventory) {
        this@TransferFragment.verifyToInit(
            {
                listener?.onTransfer(officeInventory)
                dismiss()
            },
            {
                showError(
                    getString(R.string.common_error),
                    getString(R.string.error_not_valid_finger)
                )
            },
            {
                showError(
                    getString(R.string.common_error),
                    getString(R.string.common_unexpected_error)
                )
            }
        )
    }

    override fun showAwait(title: String?, message: String?) {}

    override fun onSaveRequire(title: String?, message: String?, data: Any?) {}

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}