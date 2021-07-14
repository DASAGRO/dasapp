package kz.das.dasaccounting.ui.parent_bottom.profile.history

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryAcceptedBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryAcceptedFragment: BaseFragment<HistoryAcceptedVM, FragmentProfileHistoryAcceptedBinding>() {



    override val mViewModel: HistoryAcceptedVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryAcceptedBinding.inflate(layoutInflater)

    override fun setupUI() {

    }


}