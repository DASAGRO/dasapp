package kz.das.dasaccounting.ui.drivers.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.dialogs.ActionInventoryConfirmDialog
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.driver.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.drivers.getTsTypeImage
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class TransferConfirmFragment: BaseFragment<TransferConfirmVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val TRANSPORT_INVENTORY = "inventory"

        fun getScreen(transportInventory: TransportInventory) = DasAppScreen(TransferConfirmFragment()).apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, transportInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: TransferConfirmVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setTransportInventory(getTransportInventory())
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            btnReady.setOnClickListener {
                showConfirmDialog()
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setTsTypeImage(it)
                mViewBinding.tvInventoryTitle.text = it.model
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.gov_number) +
                            " " + it.stateNumber)
                try {
                    val inventory = it.toEntity()
                    inventory.requestId = UUID.randomUUID().toString()
                    mViewBinding.ivQr.setImageBitmap(DriverInventoryTypeConvertor().transportTransportToString(inventory).generateQR())
                } catch (e: Exception) { }
            }
        })

        mViewModel.isTransportInventorySent().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success),
                    if (mViewModel.getTransportInventory().value?.tsType.toString() == TransportType.TRAILED.type) {
                        getString(R.string.transport_accessory_inventory_transferred_successfully)
                    } else {
                        getString(R.string.transport_inventory_transferred_successfully)
                    }
                )
                requireRouter().exit()
            }
        })

        mViewModel.isOnAwait().observe(viewLifecycleOwner, Observer {
            if (it) {
                showAwait(getString(R.string.common_banner_await),
                    if (mViewModel.getTransportInventory().value?.tsType.toString() == TransportType.TRAILED.type) {
                        "Передача ПО в ожидании!"
                    } else {
                        "Передача ТС в ожидании!"
                    }
                )
                requireRouter().exit()
            }
        })
    }

    private fun showConfirmDialog() {
        val actionDialog = ActionInventoryConfirmDialog.Builder()
            .setCancelable(true)
            .setTitle(mViewBinding.tvInventoryTitle.text)
            .setDescription(mViewBinding.tvInventoryDesc.text)
            .setImage(mViewModel.getTransportInventory().value?.getTsTypeImage() ?: R.drawable.ic_tractor)
            .setOnConfirmCallback(object : ActionInventoryConfirmDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    mViewModel.sendInventory()
                }
                override fun onCancelClicked() { }
            }).build()
        actionDialog.show(childFragmentManager, ActionInventoryConfirmDialog.TAG)
    }

    private fun getTransportInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}