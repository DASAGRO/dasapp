package kz.das.dasaccounting.ui.office

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
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.office.toAccepted
import kz.das.dasaccounting.domain.data.office.toSent
import kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoFragment
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFormalizeFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFragment
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class OfficeBottomNavigationFragment: CoreBottomNavigationFragment() {

    private val officeBottomNavigationVM: OfficeBottomNavigationVM by viewModel()
    private val mNetworkConnectionVM: NetworkConnectionVM by sharedViewModel()

    private var operationsAdapter: OfficeOperationsAdapter? = null

    companion object {
        fun getScreen() = DasAppScreen(OfficeBottomNavigationFragment())
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

        operationsAdapter = OfficeOperationsAdapter(requireContext(), arrayListOf())
        mViewBinding.rvOperations.run {
            adapter = operationsAdapter
        }
        operationsAdapter?.setOfficeOperationsAdapterEvent(object : OfficeOperationsAdapter.OnOfficeOperationsAdapterEvent {
            override fun onOperationAct(operationAct: OperationAct) {
                initAcceptOperation()
            }
            override fun onOperationInit(operationAct: OperationAct) {
                initAcceptOperation()
            }

            override fun onInventoryTransfer(officeInventory: OfficeInventory) {
                val inventoryTransferDialog = TransferFormalizeFragment.newInstance(officeInventory)
                inventoryTransferDialog.setOnTransferCallback(object : TransferFormalizeFragment.OnTransferCallback {
                    override fun onTransfer(officeInventory: OfficeInventory) {
                        showTransferDialog(officeInventory)
                    }
                })
                inventoryTransferDialog.show(childFragmentManager, inventoryTransferDialog.tag)
            }
        })
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mNetworkConnectionVM.getResult().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                officeBottomNavigationVM.initAwaitRequests()
            }
        })

        mViewModel.isRefresh().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                officeBottomNavigationVM.refresh()
            }
        })

        mViewModel.isStartWorkWithQrLV().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val qrFragment = QrFragment.Builder()
                    .setCancelable(true)
                    .setOnScanCallback(object : QrFragment.OnScanCallback {
                        override fun onScan(qrScan: String) {
                            mViewModel.startWork(qrScan)
                        }
                    })
                    .build()
                qrFragment.show(childFragmentManager, "QrShiftFragment")
            }
        })

//        officeBottomNavigationVM.getOperations().observe(viewLifecycleOwner, Observer {
//            operationsAdapter?.putItems(getOperations(it))
//        })

        officeBottomNavigationVM.getOperationsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.clearOperationItems()
            operationsAdapter?.putItems(getOperations(it))
        })

        officeBottomNavigationVM.getAwaitAcceptedOperationsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeItem(OperationHead(getString(R.string.await_accepted_operations)))
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.addItems(getAwaitAcceptedOperations(it))
            } else {
                operationsAdapter?.clearAwaitAcceptedOperations()
            }
        })

        officeBottomNavigationVM.getAwaitSentOperationsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeItem(OperationHead(getString(R.string.await_sent_operations)))
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.addItems(getAwaitSentOperations(it))
            } else {
                operationsAdapter?.clearAwaitSentOperations()
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
                            if (!qrScan.contains("stateNumber") || !qrScan.contains("storeUUID") || !qrScan.contains("sealNumber") || !qrScan.contains("model")) {
                                OfficeInventoryEntityTypeConvertor().stringToOfficeInventory(qrScan)?.toDomain()?.let {
                                    requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen(it))
                                }
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

    private fun showTransferDialog(officeInventory: OfficeInventory) {
        val transferFragment = TransferFragment.newInstance(officeInventory)
        transferFragment.setOnTransferCallback(object : TransferFragment.OnTransferCallback {
            override fun onTransfer(officeInventory: OfficeInventory) {
                requireRouter().navigateTo(TransferConfirmFragment.getScreen(officeInventory))
            }
        })
        transferFragment.show(childFragmentManager, transferFragment.tag)
    }

    private fun getOperations(inventories: ArrayList<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.addAll(arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationHead(getString(R.string.inventory_title)),
        ))
        operations.addAll(inventories)
        return operations
    }

    private fun getOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.addAll(arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationHead(getString(R.string.inventory_title)),
        ))
        operations.addAll(inventories)
        return operations
    }

    private fun getAwaitSentOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_sent_operations)))
        operations.addAll(inventories.map { it.toSent() })
        return if (inventories.isEmpty()) arrayListOf() else operations
    }

    private fun getAwaitAcceptedOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_accepted_operations)))
        operations.addAll(inventories.map { it.toAccepted() })
        return if (inventories.isEmpty()) arrayListOf() else operations
    }

}