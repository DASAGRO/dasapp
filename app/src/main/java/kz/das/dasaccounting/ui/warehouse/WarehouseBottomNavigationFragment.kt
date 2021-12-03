package kz.das.dasaccounting.ui.warehouse

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.shared.NetworkConnectionVM
import kz.das.dasaccounting.data.entities.warehouse.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.WarehouseInventoryTypeConvertor
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseDetailFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class WarehouseBottomNavigationFragment: CoreBottomNavigationFragment() {

    private val warehouseBottomNavigationVM: WarehouseBottomNavigationVM by viewModel()
    private val mNetworkConnectionVM: NetworkConnectionVM by sharedViewModel()

    private var operationsAdapter: WarehousesAdapter? = null

    companion object {
        fun getScreen() = DasAppScreen(WarehouseBottomNavigationFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mViewBinding.fabQr.setOnClickListener {
            if (!CameraUtils.isPermissionGranted(requireContext())) {
                requestCameraPermissionsLaunch.launch(Manifest.permission.CAMERA)
            } else {
                initAcceptOperation()
            }
        }

        operationsAdapter = WarehousesAdapter(requireContext(), arrayListOf())
        mViewBinding.rvOperations.run {
            adapter = operationsAdapter
        }
        operationsAdapter?.setWarehouseOperationsAdapterEvent(object : WarehousesAdapter.OnWarehouseOperationsAdapterEvent {

            override fun onOperationInit(operationAct: OperationAct) {
                initAcceptOperation()
            }

            override fun onInventoryTransfer(warehouseInventory: WarehouseInventory) {
                if (!warehouseBottomNavigationVM.isHaveSavedInventory()) {
                    requireRouter().navigateTo(WarehouseDetailFragment.getScreen(warehouseInventory))
                } else {
                    showExistInventory()
                }
            }

        })

        operationsAdapter?.putItems(arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять склад", R.drawable.ic_add)))

    }

    override fun observeLiveData() {
        super.observeLiveData()

        mNetworkConnectionVM.getResult().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                warehouseBottomNavigationVM.refresh()
            }
        })

        warehouseBottomNavigationVM.getWarehouses().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.warehouse_title_head)))
            if (it.isNotEmpty()) {
                operationsAdapter?.clearOperations()
                operationsAdapter?.putItems(getOperations(it))
            }
        })

        mViewModel.isRefresh().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                warehouseBottomNavigationVM.refresh()
            }
        })
    }

    private fun initAcceptOperation() {
        val qrFragment = QrFragment.Builder()
            .setCancelable(true)
            .setOnScanCallback(object : QrFragment.OnScanCallback {
                override fun onScan(qrScan: String) {
                    delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                        try {
                            if (qrScan.contains("sealNumber") && qrScan.contains("storeUUID") && !qrScan.contains("model") && !qrScan.contains("stateNumber")) {
                                WarehouseInventoryTypeConvertor().stringToWarehouseInventory(qrScan)?.toDomain()?.let {
                                    requireRouter().navigateTo(kz.das.dasaccounting.ui.warehouse.accept.AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } else {
                                showError(getString(R.string.common_error), "Принимается только склад!")
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

    private fun getOperations(inventories: List<WarehouseInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.warehouse_title_head)))
        operations.addAll(inventories)
        return operations
    }

}