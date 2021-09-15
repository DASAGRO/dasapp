package kz.das.dasaccounting.ui.drivers.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.dialogs.ActionInventoryConfirmDialog
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.driver.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.drivers.getTsTypeImage
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptTransportCheckFragment: BaseFragment<AcceptTransportCheckVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val TRANSPORT_INVENTORY = "inventory"

        fun getScreen(transportInventory: TransportInventory) = DasAppScreen(AcceptTransportCheckFragment()).apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, transportInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: AcceptTransportCheckVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {

        mViewModel.setTransportInventory(getTransportInventory())
        mViewBinding.apply {
            mViewBinding.tvWarning.text = getString(R.string.barcode_bottom_text_reverse_scan)
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
                    ((getString(R.string.gov_number) +
                            " " + it.stateNumber) + "\n" +
                            String.format((getString(R.string.from_namespace)), it.senderName) + "\n" +
                            String.format((getString(R.string.to_namespace)), it.receiverName))
                try {
                    val inventory = mViewModel.getLocalInventory()?.toEntity()
                    inventory?.senderUUID = mViewModel.getUser()?.userId
                    mViewBinding.ivQr.setImageBitmap(DriverInventoryTypeConvertor().transportTransportToString(inventory).generateQR())
                    inventory?.let { inventoryTransport -> mViewModel.setLocalInventory(inventoryTransport.toDomain()) }
                } catch (e: Exception) { }
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
                    mViewModel.getLocalInventory()?.let {
                        requireRouter().replaceScreen(AcceptTransportConfirmationFragment.getScreen(it))
                    }
                }
                override fun onCancelClicked() { }
            }).build()
        actionDialog.show(childFragmentManager, ActionInventoryConfirmDialog.TAG)
    }

    private fun getTransportInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}