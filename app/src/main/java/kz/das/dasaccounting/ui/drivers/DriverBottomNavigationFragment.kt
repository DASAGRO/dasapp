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
import kz.das.dasaccounting.domain.data.drivers.*
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.office.toAccepted
import kz.das.dasaccounting.domain.data.office.toSent
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
                if (transportInventory.model.toUpperCase(Locale.getDefault()).contains("НАКОПИТЕЛЬ")) {
                    showFligelTransportTransferDialog(transportInventory)
                } else {
                    showTransportTransferDialog(transportInventory)
                }
            }
        })

        operationsAdapter?.putItems(arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТЦ/ПО/ТМЦ", R.drawable.ic_add)))

//        operationsAdapter?.addItems(arrayListOf(
//            OperationHead(getString(R.string.transport_tracktor_title))))
//        operationsAdapter?.addItems(arrayListOf(
//            DriverInventoryTypeConvertor().stringToTransportInventory("  {\n" +
//                    "    \"comment\": \"\",\n" +
//                    "    \"dateTime\": \"\",\n" +
//                    "    \"id\": 0,\n" +
//                    "    \"latitude\": 0,\n" +
//                    "    \"longitude\": 0,\n" +
//                    "    \"model\": \"Накопитель есь же просто\",\n" +
//                    "    \"molUuid\": \"Пушка\",\n" +
//                    "    \"stateNumber\": \"Abz-07 123\",\n" +
//                    "    \"tsType\": \"ПО\",\n" +
//                    "    \"uuid\": \"alsjkdf-asdas-dasdas-gerw\"\n" +
//                    "  }")!!.toDomain(),
//            DriverInventoryTypeConvertor().stringToTransportInventory("{\n" +
//                       "  \"comment\": \"\",\n" +
//                       "  \"dateTime\": \"\",\n" +
//                       "  \"id\": 0,\n" +
//                       "  \"latitude\": 0,\n" +
//                       "  \"longitude\": 0,\n" +
//                       "  \"model\": \"Трактор есь же просто\",\n" +
//                       "  \"molUuid\": \"Пушка\",\n" +
//                       "  \"stateNumber\": \"Abz-07 123\",\n" +
//                       "  \"tsType\": \"ТС\",\n" +
//                       "  \"uuid\": \"alsjkdf-asdas-dasdas-gerw\"\n" +
//                       "}"
//            )!!.toDomain()
//        ))
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
            operationsAdapter?.removeHead(OperationHead(getString(R.string.inventory_title)))
            operationsAdapter?.clearOperations()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getOperations(it))
            }
        })

        mViewModel.isRefresh().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                driverBottomNavigationVM.refresh()
            }
        })

        driverBottomNavigationVM.getTransportsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.transport_tracktor_title)))
            operationsAdapter?.removeHead(OperationHead(getString(R.string.transport_trailer_title)))
            operationsAdapter?.clearTransports()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getTransports(it))
            }
        })

        driverBottomNavigationVM.getAwaitAcceptedOperationsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.await_accepted_operations)))
            operationsAdapter?.clearAwaitAcceptedOperations()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getAwaitAcceptedOperations(it))
            }
        })

        driverBottomNavigationVM.getAwaitSentOperationsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.await_sent_operations)))
            operationsAdapter?.clearAwaitSentOperations()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getAwaitSentOperations(it))
            }
        })

        driverBottomNavigationVM.getAwaitAcceptedTransportsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.await_accepted_transports)))
            operationsAdapter?.clearAwaitAcceptedTransports()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getAwaitAcceptedTransports(it))
            }
        })

        driverBottomNavigationVM.getAwaitSentTransportsLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.await_sent_transports)))
            operationsAdapter?.clearAwaitSentTransports()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getAwaitSentTransports(it))
            }
        })

        driverBottomNavigationVM.getAwaitFligelDataLocally().observe(viewLifecycleOwner, Observer {
            operationsAdapter?.removeHead(OperationHead(getString(R.string.await_fligel_data)))
            operationsAdapter?.clearAwaitFligelData()
            if (it.isNotEmpty()) {
                operationsAdapter?.addItems(getAwaitFligelData(it))
            }
        })

    }

    private fun initAcceptOperation() {
        val qrFragment = QrFragment.Builder()
            .setCancelable(true)
            .setOnScanCallback(object : QrFragment.OnScanCallback {
                override fun onScan(qrScan: String) {
                    delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                        if (qrScan.contains("model") || qrScan.contains("stateNumber")) {
                            try {
                                DriverInventoryTypeConvertor().stringToTransportInventory(qrScan)?.toDomain()?.let {
                                    requireRouter().navigateTo(kz.das.dasaccounting.ui.drivers.accept.AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } catch (e: Exception) {
                                showError(getString(R.string.common_error), getString(R.string.common_error_scan))
                            }
                        } else {
                            try {
                                OfficeInventoryEntityTypeConvertor().stringToOfficeInventory(qrScan)?.toDomain()?.let {
                                    requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen(it))
                                }
                            } catch (e: Exception) {
                                showError(getString(R.string.common_error), getString(R.string.common_error_scan))
                            }
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
                showTransportTransferDialog(transportInventory)
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
        if (inventories.filter {  it.tsType == TransportType.TRANSPORT.type }.isNotEmpty()) {
            operations.add(OperationHead(getString(R.string.transport_tracktor_title)))
            operations.addAll(inventories.filter { it.tsType == TransportType.TRANSPORT.type })
        }
        if (inventories.filter {  it.tsType == TransportType.TRAILED.type }.isNotEmpty()) {
            operations.add(OperationHead(getString(R.string.transport_trailer_title)))
            operations.addAll(inventories.filter { it.tsType == TransportType.TRAILED.type })
        }
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

    private fun getAwaitSentTransports(inventories: List<TransportInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_sent_transports)))
        inventories.forEach {
            it.isPending
        }
        operations.addAll(inventories.map { it.toSent() })
        return if (inventories.isEmpty()) arrayListOf() else operations
    }

    private fun getAwaitAcceptedTransports(inventories: List<TransportInventory>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_accepted_transports)))
        inventories.forEach {
            it.isPending
        }
        operations.addAll(inventories.map { it.toAccepted() })
        return if (inventories.isEmpty()) arrayListOf() else operations
    }

    private fun getAwaitFligelData(inventories: List<FligelProduct>): ArrayList<OperationAct> {
        val operations: ArrayList<OperationAct> = arrayListOf()
        operations.add(OperationHead(getString(R.string.await_fligel_data)))
        operations.addAll(inventories.map { it.toAwait() })
        return if (inventories.isEmpty()) arrayListOf() else operations
    }

}