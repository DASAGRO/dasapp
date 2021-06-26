package kz.das.dasaccounting.ui.drivers.fligel

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.driver.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import org.koin.android.viewmodel.ext.android.viewModel

class TransferConfirmFligelDataFragment: BaseFragment<TransferConfirmFligelDataVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val FLIGEL_DATA = "fligel_data"

        fun getScreen(fligelProduct: FligelProduct) = DasAppScreen(TransferConfirmFligelDataFragment()).apply {
            val args = Bundle()
            args.putParcelable(FLIGEL_DATA, fligelProduct)
            this.setArgs(args)
        }
    }

    override val mViewModel: TransferConfirmFligelDataVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getTransportInventory())
        mViewBinding.apply {
            btnReady.setOnClickListener {
                mViewModel.receiveFligelData()
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.model
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.gov_number) +
                            " " + it.stateNumber)
                try {
                    mViewBinding.ivQr.setImageBitmap(DriverInventoryTypeConvertor().transportTransportToString(it.toEntity()).generateQR())
                } catch (e: Exception) { }
            }
        })

        mViewModel.isTransportInventorySent().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success), getString(R.string.office_inventory_transferred_successfully))
                requireRouter().exit()
            }
        })
    }


    private fun getTransportInventory(): FligelProduct? {
        return arguments?.getParcelable(FLIGEL_DATA)
    }
}