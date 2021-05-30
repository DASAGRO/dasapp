package kz.das.dasaccounting.ui.office

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.parent_bottom.ParentBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class OfficeBottomNavigationFragment: ParentBottomNavigationFragment() {

    private val officeBottomNavigationVM: OfficeBottomNavigationVM by viewModel()

    companion object {
        fun getScreen() = DasAppScreen(OfficeBottomNavigationFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mViewBinding.rvOperations.run {

        }
    }

}