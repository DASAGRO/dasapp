package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.utils.clearLogs
import kz.das.dasaccounting.databinding.FragmentProfilePassResetBinding
import kz.das.dasaccounting.utils.AppConstants
import org.koin.android.viewmodel.ext.android.viewModel

class ProfilePassResetFragment: BaseFragment<ProfilePassResetVM, FragmentProfilePassResetBinding>() {

    companion object {
        private const val TYPE = "type"

        fun getScreen(type: Int) = DasAppScreen(ProfilePassResetFragment()).apply {
            val arguments = Bundle()
            arguments.putInt(TYPE, type)
            setArgs(arguments)
        }
    }

    override val mViewModel: ProfilePassResetVM by viewModel()

    override fun getViewBinding() = FragmentProfilePassResetBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.run {
            when(getType()) {
                AppConstants.EXIT -> tvPassInfo.text = getString(R.string.enter_pass_for_exit)
                AppConstants.PASS_RESET -> tvPassInfo.text = getString(R.string.enter_pass_info_)
            }

            edtPass.addTextChangedListener {
                btnConfirm.isEnabled = it?.length == 6
            }

            toolbar.setNavigationOnClickListener { requireRouter().exit() }

            btnConfirm.setOnClickListener {
                when(getType()) {
                    AppConstants.EXIT -> checkStaticPass(mViewBinding.edtPass.text.toString())
                    AppConstants.PASS_RESET -> mViewModel.checkPassword(mViewBinding.edtPass.text.toString())
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isValidPassword().observe(viewLifecycleOwner, Observer {
            if (it) {
                requireRouter().replaceScreen(ProfilePassResetConfirmFragment.getScreen())
            }
        })
    }

    private fun checkStaticPass(pass: String) {
        if (mViewModel.checkStaticPass(pass)) {
            clearLogs(requireContext())
            onLogout()
        } else {
            showError(getString(R.string.common_error), getString(R.string.static_pass_incorrect))
        }
    }

    private fun getType(): Int {
        return arguments?.getInt(TYPE) ?: AppConstants.EXIT
    }
}