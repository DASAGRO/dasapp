package kz.das.dasaccounting.ui.auth.login

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.extensions.setPhoneFormatter
import kz.das.dasaccounting.databinding.FragmentLoginBinding
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetFragment
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment: BaseFragment<LoginVM, FragmentLoginBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(LoginFragment())
    }

    override val mViewModel: LoginVM by viewModel()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        setupPhoneField()
        mViewBinding.btnConfirm.setOnClickListener {
            mViewModel.checkUser()
        }

        mViewBinding.tvPasswordReset.setOnClickListener {
            requireRouter().navigateTo(PhonePassResetFragment.getScreen())
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.run {
            isValidPhoneNumberLV.observe(viewLifecycleOwner, Observer {
                mViewBinding.btnConfirm.isEnabled = it
            })

            isLoginExist().observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    resetNumberAttempts()
                    requireRouter().navigateTo(PassEnterFragment.getScreen(it))
                } else {
                    showError(getString(R.string.common_error), getString(R.string.error_not_exist))
                }
            })

            numberAttemptsLV().observe(viewLifecycleOwner, { it?.let {
                if (it <= 5)
                    checkUser()
                else
                    showError(
                        getString(R.string.common_error),
                        getString(R.string.common_error_could_not_connect_to_server)
                    )
            }})
        }
    }

    private fun setupPhoneField() {
        mViewBinding.edtPhone.apply {
            setPhoneFormatter()
            requestFocus()
            setText(getString(R.string.common_phone_mask))
            setSelection(this.length())
            addTextChangedListener {
                doOnTextChanged { text, _, _, _ ->
                    mViewModel.onLoginTextChanged(text.toString())
                }
            }
        }
    }


}