package kz.das.dasaccounting.ui.profile

import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileInfoBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileInfoFragment: BaseFragment<ProfileInfoVM, FragmentProfileInfoBinding>() {

    override val mViewModel: ProfileInfoVM by viewModel()

    override fun getViewBinding() = FragmentProfileInfoBinding.inflate(layoutInflater)

    override fun setupUI() {

    }
}