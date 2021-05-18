package kz.das.dasaccounting.ui.auth.password_reset

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentPasswordResetBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PassResetFragment: BaseFragment<PassResetVM, FragmentPasswordResetBinding>() {

    override val mViewModel: PassResetVM by viewModel()

    override fun getViewBinding() = FragmentPasswordResetBinding.inflate(layoutInflater)

    override fun setupUI() {

    }

}