package kz.das.dasaccounting.ui.saved_inventory

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_delete_saved_inventory.*
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentDeleteSavedInventoryBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DeleteSavedInventoryFragment :
    BaseFragment<DeleteSavedInventoryVM, FragmentDeleteSavedInventoryBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(DeleteSavedInventoryFragment())
    }

    override val mViewModel: DeleteSavedInventoryVM by viewModel()

    override fun getViewBinding() = FragmentDeleteSavedInventoryBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }

            btnConfirm.setOnClickListener {
                mViewModel.demicalCodeLiveData.value?.let {
                    val usersPin = edt_pass.text.toString().replace(("[^\\d.]").toRegex(), "")
                    if (usersPin == it) {
                        mViewModel.deleteSavedInventory()
                        requireRouter().exit()
                        showSuccess(getString(R.string.common_banner_success), getString(R.string.success_deleted_inventory))
                    } else {
                        showError(getString(R.string.common_error), getString(R.string.static_pass_incorrect))
                    }
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.hexadecimalCodeLiveData.observe(viewLifecycleOwner, {
            tv_pin_code.text = "PIN: ${it}"
        })

        mViewModel.demicalCodeLiveData.observe(viewLifecycleOwner, {
            edt_pass.setText("${it.substring(0, it.length - 3)}")
        })
    }

}