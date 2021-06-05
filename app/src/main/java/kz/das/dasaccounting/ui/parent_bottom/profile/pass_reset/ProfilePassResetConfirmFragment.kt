package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfilePassResetConfirmBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ProfilePassResetConfirmFragment : BaseFragment<ProfilePassResetConfirmVM, FragmentProfilePassResetConfirmBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfilePassResetConfirmFragment())
    }

    override val mViewModel: ProfilePassResetConfirmVM by viewModel()

    override fun getViewBinding() = FragmentProfilePassResetConfirmBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.run {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }
            mViewBinding.btnConfirm.setOnClickListener { mViewModel.checkPassword(mViewBinding.edtPass.text.toString(), mViewBinding.edtPassConfirm.text.toString()) }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isPassUpdated().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.password_change), getString(R.string.password_changed_successfully))
                requireRouter().exit()
            }
        })
    }
}