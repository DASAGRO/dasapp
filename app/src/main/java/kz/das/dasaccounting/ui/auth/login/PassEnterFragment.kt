package kz.das.dasaccounting.ui.auth.login

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PassEnterFragment: BaseFragment<PassEnterVM, FragmentPasswordBinding>() {

    override val mViewModel: PassEnterVM by viewModel()

    override fun getViewBinding() = FragmentPasswordBinding.inflate(layoutInflater)

    override fun setupUI() {

    }

}