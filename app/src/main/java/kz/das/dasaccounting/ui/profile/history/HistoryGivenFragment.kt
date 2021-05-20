package kz.das.dasaccounting.ui.profile.history

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI() {

    }


}