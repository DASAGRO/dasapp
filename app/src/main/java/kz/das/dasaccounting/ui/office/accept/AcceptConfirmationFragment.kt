package kz.das.dasaccounting.ui.office.accept

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptConfirmationFragment : BaseFragment<AcceptConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(AcceptConfirmationFragment())
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