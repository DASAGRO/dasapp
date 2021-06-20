package kz.das.dasaccounting.ui.office

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoFragment
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
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
        operationsAdapter = OfficeOperationsAdapter(requireContext(), arrayListOf())
        mViewBinding.rvOperations.run {
            adapter = operationsAdapter
        }
        operationsAdapter?.putItems(getOperations())
        operationsAdapter?.setOfficeOperationsAdapterEvent(object : OfficeOperationsAdapter.OnOfficeOperationsAdapterEvent {
            override fun onOperationAct(operationAct: OperationAct) {
                //requireRouter().navigateTo(AcceptInfoFragment.getScreen())
            }
            override fun onOperationInit(operationAct: OperationAct) {
                requireRouter().navigateTo(AcceptInventoryInfoFragment.getScreen())
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

    private fun getOperations(): ArrayList<OperationAct> {
        return arrayListOf(
            OperationHead(getString(R.string.available_operations)),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationHead(getString(R.string.inventory_title))
        )
    }

}