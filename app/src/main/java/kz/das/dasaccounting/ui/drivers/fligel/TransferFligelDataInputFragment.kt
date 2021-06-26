package kz.das.dasaccounting.ui.drivers.fligel

import android.os.Bundle
import co.infinum.goldfinger.Goldfinger
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetGatherInputBinding
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFligelDataInputFragment: BaseBottomSheetFragment<FragmentBottomSheetGatherInputBinding, TransferFligelDataInputVM>() {

    companion object {

        private const val TRANSPORT_INVENTORY = "transport_inventory"

        fun newInstance(fligelProduct: FligelProduct) = TransferFligelDataInputFragment().apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, fligelProduct)
            this.arguments = args
        }

    }

    private var listener: OnTransferCallback ?= null

    interface OnTransferCallback {
        fun onTransfer(fligelProduct: FligelProduct)
    }

    fun setOnTransferCallback(listener: OnTransferCallback) {
        this.listener = listener
    }

    override val mViewModel: TransferFligelDataInputVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetGatherInputBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getOfficeInventory())
        mViewBinding.apply {
            this.ivClose.setOnClickListener {
                dismiss()
            }
            this.btnMakeTransfer.setOnClickListener {
                getOfficeInventory()?.let {
                    if (!edtFieldNumber.text.isNullOrEmpty() &&
                        !edtGatherWet.text.isNullOrEmpty() &&
                        !edtGatherWeight.text.isNullOrEmpty() &&
                        !edtTransportType.text.isNullOrEmpty()) {
                        checkConfirmation(FligelProduct(
                            (0 until 10000).random(),
                            edtTransportType.text.toString(),
                            "Отправка урожая",
                            edtFieldNumber.text.toString().toInt(),
                            edtGatherWeight.text.toString().toInt(),
                            edtGatherWet.text.toString().toInt(),
                            "Название урожая"
                        ))
                    } else {
                        showError(getString(R.string.common_error), getString(R.string.common_fill_all_inputs))
                    }
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
    }

    private fun checkConfirmation(officeInventory: FligelProduct) {
        val goldfinger = Goldfinger.Builder(requireContext()).build()
        if (goldfinger.canAuthenticate()) {
            val params = Goldfinger.PromptParams.Builder(requireActivity())
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

    private fun getOfficeInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }

}