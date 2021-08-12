package kz.das.dasaccounting.ui.auth.password_reset

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.extensions.setPhoneFormatter
import kz.das.dasaccounting.databinding.FragmentPhonePasswordResetBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PhonePassResetFragment: BaseFragment<PhonePassResetVM, FragmentPhonePasswordResetBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(PhonePassResetFragment())
    }

    override val mViewModel: PhonePassResetVM by viewModel()

    override fun getViewBinding() = FragmentPhonePasswordResetBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        setupPhoneField()
        mViewBinding.toolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        mViewBinding.btnConfirm.setOnClickListener {
            mViewModel.checkUser()
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isValidPhoneNumberLV.observe(viewLifecycleOwner, Observer {
            mViewBinding.btnConfirm.isEnabled = it
        })

        mViewModel.isLoginExist().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                requireRouter().newRootScreen(PassResetFragment.getScreen(it))
            } else {
                showError(getString(R.string.common_error), getString(R.string.error_not_exist))
            }
        })
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