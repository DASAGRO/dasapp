package kz.das.dasaccounting.ui.office.transfer

import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetWarehouseTransferBinding
import org.koin.android.viewmodel.ext.android.viewModel

class TransferFormalizeFragment: BaseBottomSheetFragment<FragmentBottomSheetWarehouseTransferBinding, TransferFormalizeVM>() {

    override val mViewModel: TransferFormalizeVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetWarehouseTransferBinding.inflate(layoutInflater)

    override fun setupUI() {
        TODO("Not yet implemented")
    }

    override fun showAwait(title: String?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) {
        TODO("Not yet implemented")
    }
}