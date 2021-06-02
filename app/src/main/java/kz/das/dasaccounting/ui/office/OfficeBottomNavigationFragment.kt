package kz.das.dasaccounting.ui.office

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.ui.office.accept.AcceptInfoFragment
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
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
                requireRouter().navigateTo(AcceptInfoFragment.getScreen())
            }
        })
    }

    private fun getOperations(): ArrayList<OperationAct> {
        return arrayListOf(
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add),
            OperationInit("Принять ТМЦ", R.drawable.ic_add)
        )
    }

}