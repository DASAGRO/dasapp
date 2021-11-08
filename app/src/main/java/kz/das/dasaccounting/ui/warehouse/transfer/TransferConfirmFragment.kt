package kz.das.dasaccounting.ui.warehouse.transfer

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.dialogs.ActionInventoryConfirmDialog
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.warehouse.toDomain
import kz.das.dasaccounting.data.entities.warehouse.toEntity
import kz.das.dasaccounting.data.source.local.typeconvertors.WarehouseInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentBarcodeGenerateBinding
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.utils.MediaPlayerUtils
import kz.das.dasaccounting.ui.warehouse.WarehouseBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class TransferConfirmFragment: BaseFragment<TransferConfirmVM, FragmentBarcodeGenerateBinding>() {

    companion object {
        private const val WAREHOUSE = "warehouse"
        private const val FILE_IDS = "file_ids"

        fun getScreen(warehouseInventory: WarehouseInventory?, fileIds: ArrayList<Int>?) = DasAppScreen(TransferConfirmFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE, warehouseInventory)
            args.putIntegerArrayList(FILE_IDS, fileIds)
            this.setArgs(args)
        }
    }

    override val mViewModel: TransferConfirmVM by viewModel()

    override fun getViewBinding() = FragmentBarcodeGenerateBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setFileIds(getFileIds())
        mViewBinding.apply {
            tvWarning.text = getString(R.string.barcode_next_bottom_text)
            btnConfirm.text = getString(R.string.next)

            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            btnConfirm.setOnClickListener {
                showConfirmDialog()
            }
        }

        mViewModel.apply {
            getSavedInventory(InventoryType.WAREHOUSE)?.let {
                it as WarehouseInventory

                mViewModel.setWarehouseInventory(it)
                mViewBinding.ivQr.setImageBitmap(WarehouseInventoryTypeConvertor().warehouseToString(it.toEntity()).generateQR())
            } ?: run {
                mViewModel.setWarehouseInventory(getWarehouse())
                try {
                    val inventory = mViewModel.getLocalInventory()?.toEntity()
                    inventory?.requestId = UUID.randomUUID().toString()
                    inventory?.senderUUID = mViewModel.getUser()?.userId
                    mViewBinding.ivQr.setImageBitmap(WarehouseInventoryTypeConvertor().warehouseToString(inventory).generateQR())

                    inventory?.let{ mViewModel.saveInventory(it.toDomain(), InventoryType.WAREHOUSE) }
                } catch (e: Exception) { }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getWarehouseInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_warehouse)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.seal_number) +
                            " " + it.sealNumber)
            }
        })

        mViewModel.isWarehouseInventorySent().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success), "Склад успешно передан")
                MediaPlayerUtils.playSuccessSound(requireContext())
                requireRouter().newRootScreen(Screens.getRoleScreens(mViewModel.getUserRole() ?: "") ?: WarehouseBottomNavigationFragment.getScreen())
            }
        })

    }

    private fun showConfirmDialog() {
        val actionDialog = ActionInventoryConfirmDialog.Builder()
            .setCancelable(true)
            .setTitle(mViewBinding.tvInventoryTitle.text)
            .setDescription(mViewBinding.tvInventoryDesc.text)
            .setImage(R.drawable.ic_warehouse)
            .setOnConfirmCallback(object : ActionInventoryConfirmDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    mViewModel.sendInventory()
                }
                override fun onCancelClicked() { }
            }).build()
        actionDialog.show(childFragmentManager, ActionInventoryConfirmDialog.TAG)
    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE)
    }

    private fun getFileIds(): ArrayList<Int> {
        return arguments?.getIntegerArrayList(FILE_IDS) ?: arrayListOf()
    }

}