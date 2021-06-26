package kz.das.dasaccounting.ui.office.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.office.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class TransferConfirmFragment: BaseFragment<TransferConfirmVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val OFFICE_INVENTORY = "inventory"

        fun getScreen(officeInventoryAccept: OfficeInventory) = DasAppScreen(TransferConfirmFragment()).apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventoryAccept)
            this.setArgs(args)
        }
    }

    override val mViewModel: TransferConfirmVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())
        mViewBinding.apply {
            btnReady.setOnClickListener {
                mViewModel.sendInventory()
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.inventory_total_quantity) +
                            " " + it.quantity +
                            " " + it.type)
                try {
                    mViewBinding.ivQr.setImageBitmap(OfficeInventoryEntityTypeConvertor().officeInventoryToString(it.toEntity()).generateQR())
                } catch (e: Exception) { }
            }
        })

        mViewModel.isOfficeInventorySent().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success), getString(R.string.office_inventory_transferred_successfully))
                requireRouter().exit()
            }
        })
    }


    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}