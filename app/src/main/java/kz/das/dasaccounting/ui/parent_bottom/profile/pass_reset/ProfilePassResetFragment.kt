package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfilePassResetBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ProfilePassResetFragment: BaseFragment<ProfilePassResetVM, FragmentProfilePassResetBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfilePassResetFragment())
    }

    override val mViewModel: ProfilePassResetVM by viewModel()

    override fun getViewBinding() = FragmentProfilePassResetBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.run {
            edtPass.addTextChangedListener {
                btnConfirm.isEnabled = it?.length == 6
            }
            toolbar.setNavigationOnClickListener { requireRouter().exit() }

            btnConfirm.setOnClickListener {
                mViewModel.checkPassword(mViewBinding.edtPass.text.toString())
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
}