package kz.das.dasaccounting.ui.warehouse.operations

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.entities.warehouse.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.data.source.local.typeconvertors.WarehouseInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentWarehouseActionsBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.warehouse.accept.AcceptInventoryInfoFragment
import org.koin.android.viewmodel.ext.android.viewModel

class WarehouseDetailFragment: BaseFragment<WarehouseDetailVM, FragmentWarehouseActionsBinding>() {

    companion object {
        private const val WAREHOUSE_INVENTORY = "WAREHOUSE"
        fun getScreen(warehouseInventory: WarehouseInventory?) = DasAppScreen(WarehouseDetailFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE_INVENTORY, warehouseInventory)
            this.setArgs(args)
        }
    }

    override val mViewModel: WarehouseDetailVM by viewModel()

    override fun getViewBinding() = FragmentWarehouseActionsBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setWarehouseInventory(getWarehouse())
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }
            tvWarehouseActionsTitle.text = mViewModel.getWarehouseInventory()?.name
            llActionList.setOnClickListener {
                requireRouter().navigateTo(WarehouseOperationsFragment.getScreen(mViewModel.getWarehouseInventory()))
            }
            llActionAdd.setOnClickListener {
                showQrDialog()
            }
            llActionSwap.setOnClickListener {
                requireRouter().navigateTo(WarehouseOptionsFragment.getScreen(getWarehouse()))
            }
        }
    }

    private fun showQrDialog() {
        val qrFragment = QrFragment.Builder()
            .setCancelable(true)
            .setOnScanCallback(object : QrFragment.OnScanCallback {
                override fun onScan(qrScan: String) {
                    delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                        try {
                            if (qrScan.contains("sealNumber") && !qrScan.contains("model") && !qrScan.contains("stateNumber")) {
                                WarehouseInventoryTypeConvertor().stringToWarehouseInventory(qrScan)?.toDomain()?.let {
                                    requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } else if (qrScan.contains("model") && qrScan.contains("stateNumber")) {
                                DriverInventoryTypeConvertor().stringToTransportInventory(qrScan)?.toDomain()?.let {
                                    val inventory = it
                                    inventory.storeUUIDReceiver = mViewModel.getWarehouseInventory()?.storeUUID
                                    requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.accept.AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } else if (qrScan.contains("name") && (!qrScan.contains("stateNumber") && !qrScan.contains("sealNumber") && !qrScan.contains("model"))) {
                                OfficeInventoryEntityTypeConvertor().stringToOfficeInventory(qrScan)?.toDomain()?.let {
                                    val inventory = it
                                    inventory.storeUUIDReceiver = mViewModel.getWarehouseInventory()?.storeUUID
                                    requireRouter().navigateTo(kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } else {
                                showError(getString(R.string.common_error), getString(R.string.common_error_scan))
                            }
                        } catch (e: Exception) {
                            showError(getString(R.string.common_error), getString(R.string.common_error_scan))
                        }
                    }
                }
            })
            .build()
        qrFragment.show(childFragmentManager, "QrAcceptFragment")
    }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE_INVENTORY)
    }

}