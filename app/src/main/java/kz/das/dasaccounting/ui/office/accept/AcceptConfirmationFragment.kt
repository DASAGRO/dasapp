package kz.das.dasaccounting.ui.office.accept

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptConfirmationFragment : BaseFragment<AcceptConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        private const val OFFICE_INVENTORY = "inventory"

        fun getScreen(officeInventoryAccept: OfficeInventory) = DasAppScreen(AcceptConfirmationFragment()).apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventoryAccept)
            this.setArgs(args)
        }
    }

    override val mViewModel: AcceptConfirmationVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())

        mViewBinding.apply {
            btnReady.setOnClickListener {

            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
    }


    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}