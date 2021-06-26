package kz.das.dasaccounting.ui.drivers

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
import kz.das.dasaccounting.data.entities.driver.toDomain
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.ui.drivers.fligel.TransferConfirmFligelDataFragment
import kz.das.dasaccounting.ui.drivers.fligel.TransferFligelDataFormalizeFragment
import kz.das.dasaccounting.ui.drivers.fligel.TransferFligelDataInputFragment
import kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoFragment
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFormalizeFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFragment
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class DriverBottomNavigationFragment: CoreBottomNavigationFragment() {

    private val driverBottomNavigationVM: DriverBottomNavigationVM by viewModel()
    private val mNetworkConnectionVM: NetworkConnectionVM by sharedViewModel()

    private var operationsAdapter: DriverOperationsAdapter? = null

    companion object {
        fun getScreen() = DasAppScreen(DriverBottomNavigationFragment())
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

        operationsAdapter = DriverOperationsAdapter(requireContext(), arrayListOf())
        mViewBinding.rvOperations.run {
            adapter = operationsAdapter
        }
        operationsAdapter?.setOfficeOperationsAdapterEvent(object : DriverOperationsAdapter.OnOfficeOperationsAdapterEvent {
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

            override fun onInventoryTransportTransfer(transportInventory: TransportInventory) {
                val inventoryTransferDialog = kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.newInstance(transportInventory)
                inventoryTransferDialog.setOnTransferCallback(object : kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.OnTransferCallback {
                    override fun onTransfer(transportInventory: TransportInventory) {
                        val isFligel = transportInventory.model.toUpperCase(Locale.getDefault()).contains("НАКОПИТЕЛЬ")
                        if (isFligel) {
                            showFligelTransportTransferDialog(transportInventory)
                        } else {
                            showTransportTransferDialog(transportInventory)
                        }
                    }
                })
                inventoryTransferDialog.show(childFragmentManager, inventoryTransferDialog.tag)
            }
        })

        operationsAdapter?.addItems(arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТЦ/ПО/ТМЦ", R.drawable.ic_add)))
    }

    override fun onResume() {
        super.onResume()
        driverBottomNavigationVM.refresh()
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mNetworkConnectionVM.getResult().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                driverBottomNavigationVM.initAwaitRequests()
            }
        })

        driverBottomNavigationVM.getOperationsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.removeItem(OperationHead(getString(R.string.inventory_title)))
                operationsAdapter?.clearItems(it)
                operationsAdapter?.addItems(getOperations(it))
            }
        })

        driverBottomNavigationVM.getTransportsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(arrayListOf(
                    OperationHead(getString(R.string.transport_tracktor_title)),
                    OperationHead(getString(R.string.transport_trailer_title))
                ))
                operationsAdapter?.clearItems(it)
                operationsAdapter?.addItems(getTransports(it))
            }
        })

        driverBottomNavigationVM.getAwaitAcceptedOperationsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.removeItem(OperationHead(getString(R.string.await_accepted_operations)))
                operationsAdapter?.addItems(getAwaitAcceptedOperations(it))
            }
        })

        driverBottomNavigationVM.getAwaitSentOperationsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.removeItem(OperationHead(getString(R.string.await_sent_operations)))
                operationsAdapter?.addItems(getAwaitSentOperations(it))
            }
        })

        driverBottomNavigationVM.getAwaitAcceptedTransportsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.removeItem(OperationHead(getString(R.string.await_accepted_transports)))
                operationsAdapter?.addItems(getAwaitAcceptedTransports(it))
            }
        })

        driverBottomNavigationVM.getAwaitSentTransportsLocally().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                operationsAdapter?.clearItems(it)
                operationsAdapter?.removeItem(OperationHead(getString(R.string.await_sent_transports)))
                operationsAdapter?.addItems(getAwaitSentTransports(it))
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
                            OfficeInventoryEntityTypeConvertor().stringToOfficeInventory(qrScan)?.toDomain()?.let {
                                requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen(it))
                            }
                        } catch (e: Exception) {
                            try {
                                DriverInventoryTypeConvertor().stringToTransportInventory(qrScan)?.toDomain()?.let {
                                    val isFligel = it.model.toUpperCase(Locale.getDefault()).contains("НАКОПИТЕЛЬ")
                                    if (isFligel) {
                                        showFligelTransportTransferDialog(it)
                                    } else {
                                        requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.accept.AcceptInventoryInfoFragment.getScreen(it))
                                    }
                                }
                            } catch (e: Exception) {
                                showError(getString(R.string.common_error), getString(R.string.common_error_scan))
                            }
                            //showError(getString(R.string.common_error), getString(R.string.common_error_scan))
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

    private fun showTransportTransferDialog(transportInventory: TransportInventory) {
        val transferFragment = kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.newInstance(transportInventory)
        transferFragment.setOnTransferCallback(object : kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.OnTransferCallback {
            override fun onTransfer(transportInventory: TransportInventory) {
                requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmFragment.getScreen(transportInventory))
            }
        })
        transferFragment.show(childFragmentManager, transferFragment.tag)
    }

    private fun showFligelTransportTransferDialog(transportInventory: TransportInventory) {
        val transferFragment = TransferFligelDataFormalizeFragment.newInstance(transportInventory)
        transferFragment.setOnTransferCallback(object : TransferFligelDataFormalizeFragment.OnTransferCallback {
            override fun onTransfer(transportInventory: TransportInventory) {
                val transferDialog = kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.newInstance(transportInventory)
                transferDialog.setOnTransferCallback(object : kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeFragment.OnTransferCallback {
                    override fun onTransfer(transportInventory: TransportInventory) {
                        requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmFragment.getScreen(transportInventory))
                    }
                })
                transferDialog.show(childFragmentManager, transferFragment.tag)
            }

            override fun onAccept(transportInventory: TransportInventory) {
                val transferDialog = TransferFligelDataInputFragment.newInstance()
                transferDialog.setOnTransferCallback(object : TransferFligelDataInputFragment.OnTransferCallback {
                    override fun onTransfer(fligelProduct: FligelProduct) {
                        requireRouter().navigateTo(TransferConfirmFligelDataFragment.getScreen(fligelProduct))
                    }
                })
                transferDialog.show(childFragmentManager, transferFragment.tag)
            }
        })
        transferFragment.show(childFragmentManager, transferFragment.tag)
    }

    private fun getOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.inventory_title)))
        operations.addAll(inventories)
        return operations
    }

    private fun getTransports(inventories: List<TransportInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.transport_tracktor_title)))
        operations.addAll(inventories.filter { it.tsType == TransportType.TRANSPORT.type })
        operations.add(OperationHead(getString(R.string.transport_trailer_title)))
        operations.addAll(inventories.filter { it.tsType == TransportType.TRANSPORT.type })
        return operations
    }

    private fun getAwaitSentOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_sent_operations)))
        operations.addAll(inventories)
        return if (inventories.isEmpty()) arrayListOf() else arrayListOf()
    }

    private fun getAwaitAcceptedOperations(inventories: List<OfficeInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_accepted_operations)))
        operations.addAll(inventories)
        return if (inventories.isEmpty()) arrayListOf()else arrayListOf()
    }

    private fun getAwaitSentTransports(inventories: List<TransportInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_sent_transports)))
        operations.addAll(inventories)
        return if (inventories.isEmpty()) arrayListOf() else arrayListOf()
    }

    private fun getAwaitAcceptedTransports(inventories: List<TransportInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_accepted_transports)))
        operations.addAll(inventories)
        return if (inventories.isEmpty()) arrayListOf() else arrayListOf()
    }

}