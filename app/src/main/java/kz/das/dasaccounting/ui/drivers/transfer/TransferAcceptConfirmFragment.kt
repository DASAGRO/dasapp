package kz.das.dasaccounting.ui.drivers.transfer

import android.os.Bundle
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptBinding
import org.koin.android.viewmodel.ext.android.viewModel

class TransferAcceptConfirmFragment: BaseFragment<TransferAcceptConfirmVM, FragmentInventoryAcceptBinding>() {


    override val mViewModel: TransferAcceptConfirmVM by viewModel()

    override fun getViewBinding(): FragmentInventoryAcceptBinding {
        TODO("Not yet implemented")
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}