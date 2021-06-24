package kz.das.dasaccounting.ui.drivers.transports

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.ui.office.accept.AcceptConfirmationVM
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptInventoryConfirmationFragment : BaseFragment<AcceptConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(AcceptInventoryConfirmationFragment())
    }

    override val mViewModel: AcceptConfirmationVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.apply {

        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
    }

}