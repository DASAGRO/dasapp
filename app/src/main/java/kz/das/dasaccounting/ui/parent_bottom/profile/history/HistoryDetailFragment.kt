package kz.das.dasaccounting.ui.parent_bottom.profile.history

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileHistoryDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryDetailFragment: BaseFragment<HistoryDetailVM, FragmentProfileHistoryDetailBinding>() {

    override val mViewModel: HistoryDetailVM by viewModel()

    override fun getViewBinding() = FragmentProfileHistoryDetailBinding.inflate(layoutInflater)

    override fun setupUI() {

    }



}