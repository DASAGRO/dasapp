package kz.das.dasaccounting.ui.office.accept

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptInfoFragment: BaseFragment<AcceptInfoVM, FragmentInventoryAcceptBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(AcceptInfoFragment())
    }

    override val mViewModel: AcceptInfoVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            btnAccept.setOnClickListener {
                requireRouter().replaceScreen(AcceptConfirmationFragment.getScreen())
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
    }
}