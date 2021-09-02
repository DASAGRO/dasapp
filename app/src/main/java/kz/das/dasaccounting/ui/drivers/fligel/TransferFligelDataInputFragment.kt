package kz.das.dasaccounting.ui.drivers.fligel

import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.core.ui.extensions.verifyToInit
import kz.das.dasaccounting.databinding.FragmentBottomSheetGatherInputBinding
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class TransferFligelDataInputFragment: BaseBottomSheetFragment<FragmentBottomSheetGatherInputBinding, TransferFligelDataInputVM>() {

    companion object {

        private const val TRANSPORT_INVENTORY = "transport_inventory"

        fun newInstance() = TransferFligelDataInputFragment()
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
        mViewBinding.apply {
            this.ivClose.setOnClickListener {
                dismiss()
            }
            this.btnMakeTransfer.setOnClickListener {
                if (!edtFieldNumber.text.isNullOrEmpty() &&
                    !edtGatherWeight.text.isNullOrEmpty() &&
                    !edtTransportType.text.isNullOrEmpty() &&
                    mViewModel.getNomenclatures().filter { it.fieldNumber == edtFieldNumber.text.toString() }.isNullOrEmpty()) {
                    showError(getString(R.string.common_error), "Не удалось найти урожай по номеру поля")
                } else if (!edtFieldNumber.text.isNullOrEmpty() &&
                    !edtGatherWeight.text.isNullOrEmpty() &&
                    !edtTransportType.text.isNullOrEmpty()) {
                    checkConfirmation(FligelProduct(
                        (0 until 10000).random(),
                        System.currentTimeMillis(),
                        mViewModel.getLocation().long,
                        mViewModel.getLocation().lat,
                        edtTransportType.text.toString(),
                        "Отправка урожая",
                        edtFieldNumber.text.toString().toInt(),
                        edtGatherWeight.text.toString().toDouble(),
                        0,
                        UUID.randomUUID().toString(),
                        mViewModel.getNomenclatures().filter { it.fieldNumber == edtFieldNumber.text.toString() }[0].name ?: "Неизвестный урожай"
                    ))
                } else {
                    showError(getString(R.string.common_error), getString(R.string.common_fill_all_inputs))
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getNomenclaturesLocally().observe(viewLifecycleOwner, Observer {
            mViewModel.setNomenclatures(it)
        })
    }

    private fun checkConfirmation(fligelProduct: FligelProduct) {
        this@TransferFligelDataInputFragment.verifyToInit(
            {
                listener?.onTransfer(fligelProduct)
                dismiss()
            },
            { showError(getString(R.string.common_error), getString(R.string.error_not_valid_finger)) },
            { showError(getString(R.string.common_error), getString(R.string.common_unexpected_error)) }
        )
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

}