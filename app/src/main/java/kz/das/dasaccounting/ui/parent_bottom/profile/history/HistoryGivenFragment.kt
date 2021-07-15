package kz.das.dasaccounting.ui.parent_bottom.profile.history

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryGivenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryGivenFragment: BaseFragment<HistoryGivenVM, FragmentProfileHistoryGivenBinding>() {

    companion object {
        fun newInstance() = HistoryGivenFragment()
    }

    override val mViewModel: HistoryGivenVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryGivenBinding.inflate(layoutInflater)

    override fun setupUI() {

    }


}