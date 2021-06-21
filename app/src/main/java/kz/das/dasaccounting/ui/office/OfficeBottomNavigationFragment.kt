package kz.das.dasaccounting.ui.office

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.data.entities.office.toDomain
import kz.das.dasaccounting.data.source.local.typeconvertors.OfficeInventoryEntityTypeConvertor
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoFragment
import kz.das.dasaccounting.ui.office.transfer.TransferFormalizeFragment
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import org.koin.android.viewmodel.ext.android.viewModel

class OfficeBottomNavigationFragment: CoreBottomNavigationFragment() {

    private val officeBottomNavigationVM: OfficeBottomNavigationVM by viewModel()
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
        operationsAdapter?.putItems(getOperations())
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

                    }
                })
                inventoryTransferDialog.show(childFragmentManager, inventoryTransferDialog.tag)
            }
        })
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isStartWorkWithQrLV().observe(viewLifecycleOwner, Observer {
            if (it) {
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
    }

    private fun initAcceptOperation() {
        val qrFragment = QrFragment.Builder()
            .setCancelable(true)
            .setOnScanCallback(object : QrFragment.OnScanCallback {
                override fun onScan(qrScan: String) {
                    val acceptInventory: OfficeInventory? =
                        OfficeInventoryEntityTypeConvertor().stringToOfficeInventory(qrScan)?.toDomain()
                    if (acceptInventory != null) {
                        requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen(acceptInventory))
                    }
                }
            })
            .build()
        qrFragment.show(childFragmentManager, "QrAcceptFragment")
    }

    private fun showTransferDialog(officeInventory: OfficeInventory) {

    }


    private fun getOperations(): ArrayList<OperationAct> {
        return arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationHead(getString(R.string.inventory_title))
        )
    }

}