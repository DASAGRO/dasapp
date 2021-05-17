package kz.das.dasaccounting.ui.auth.login

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentLoginBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment: BaseFragment<LoginVM, FragmentLoginBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(LoginFragment())
    }

    override val mViewModel: LoginVM by viewModel()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun setupUI() {

    }



}