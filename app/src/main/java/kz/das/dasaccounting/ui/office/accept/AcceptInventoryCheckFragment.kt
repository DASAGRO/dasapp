package kz.das.dasaccounting.ui.office.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.dialogs.ActionInventoryConfirmDialog
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.common.TransferItem
import kz.das.dasaccounting.data.entities.common.TransferItemTypeConvertor
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.office.toEntity
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class AcceptInventoryCheckFragment: BaseFragment<AcceptInventoryCheckVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val OFFICE_INVENTORY = "inventory"

        fun getScreen(officeInventoryAccept: OfficeInventory) = DasAppScreen(AcceptInventoryCheckFragment()).apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventoryAccept)
            this.setArgs(args)
        }
    }

    override val mViewModel: AcceptInventoryCheckVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setOfficeInventory(getOfficeInventory())
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

        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    ((getString(R.string.inventory_total_quantity) +
                            " " + it.quantity +
                            " " + it.type) + "\n" +
                            String.format((getString(R.string.from_namespace)), it.senderName) + "\n" +
                            String.format((getString(R.string.to_namespace)), it.receiverName))
                try {
                    val inventory = mViewModel.getLocalInventory()?.toEntity()
                    val transferItem = TransferItem(
                        storeUUIDReceiver = mViewModel.getLocalInventory()?.storeUUIDReceiver,
                        receiverUUID = it.receiverUUID,
                        receiverName = it.receiverName,
                        transferType = "material_type"
                    )
                    mViewBinding.ivQr.setImageBitmap(TransferItemTypeConvertor().transferItemToString(transferItem).generateQR())


                    //mViewBinding.ivQr.setImageBitmap(OfficeInventoryEntityTypeConvertor().officeInventoryToString(inventory).generateQR())
                    inventory?.let { inventoryOffice -> mViewModel.setLocalInventory(inventoryOffice.toDomain()) }
                } catch (e: Exception) { }
            }
        })

    }

    private fun showConfirmDialog() {
        val actionDialog = ActionInventoryConfirmDialog.Builder()
            .setCancelable(true)
            .setTitle(mViewBinding.tvInventoryTitle.text)
            .setDescription(mViewBinding.tvInventoryDesc.text)
            .setImage(R.drawable.ic_inventory)
            .setOnConfirmCallback(object : ActionInventoryConfirmDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    mViewModel.getLocalInventory()?.let {
                        requireRouter().replaceScreen(AcceptConfirmationFragment.getScreen(it))
                    }
                }
                override fun onCancelClicked() { }
            }).build()
        actionDialog.show(childFragmentManager, ActionInventoryConfirmDialog.TAG)
    }

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }


}