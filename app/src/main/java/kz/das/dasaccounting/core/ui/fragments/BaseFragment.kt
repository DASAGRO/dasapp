package kz.das.dasaccounting.core.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.activities.BaseActivity
import kz.das.dasaccounting.core.ui.dialogs.ActionInventoryConfirmDialog
import kz.das.dasaccounting.core.ui.dialogs.InventoryExistDialog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmFragment
import kz.das.dasaccounting.ui.saved_inventory.DeleteSavedInventoryFragment

abstract class BaseFragment<VM: BaseVM, VB: ViewBinding>(): Fragment(), ViewCallback {

    protected lateinit var mViewBinding: VB
    protected abstract val mViewModel: VM

    protected abstract fun getViewBinding(): VB
    protected abstract fun setupUI(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = getViewBinding()
        setupUI(savedInstanceState)
        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveData()
    }

    override fun onDestroyView() {
        if (mViewModel.isLoading().value == true) {
            hideLoading()
        }
        super.onDestroyView()
    }

    override fun observeLiveData() {
        mViewModel.run {
            isLoading().observe(viewLifecycleOwner, Observer { isLoading ->
                isLoading?.let {
                    if (isLoading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                }
            })

            toNavigateBack().observe(viewLifecycleOwner, Observer { toNavigateBack ->
                if (toNavigateBack) {
                    this@BaseFragment.navigateBack()
                }
            })

            getBackendResponseError().observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    (requireActivity() as BaseActivity<*, *>).showBackendResponseError(
                        error.errorCode, error.message
                    )
                }
            })

            getConnectionError().observe(viewLifecycleOwner, Observer { error ->
                if (error != null && error) {
                    (requireActivity() as BaseActivity<*, *>).showConnectionError()
                }
            })

            getUnknownError().observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    (requireActivity() as BaseActivity<*, *>).showUnknownError(error.message)
                }
            })

            getUiError().observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    (requireActivity() as BaseActivity<*, *>).showUiError(error.message)
                }
            })

            getNullResponseError().observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    (requireActivity() as BaseActivity<*, *>).showNullResponseError()
                }
            })

            getNetworkResponseError().observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    (requireActivity() as BaseActivity<*, *>).showNetworkResponseError(error.message)
                }
            })
        }
    }

    fun changeStatusColor(colorId: Int) {
        (requireActivity() as? BaseActivity<*, *>)?.changeStatusBarColor(colorId)
    }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) {
        (requireActivity() as? BaseActivity<*, *>)?.saveDataRequired(title, message, data)
    }

    override fun showSuccess(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showSuccess(title, message)
    }

    override fun showError(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showError(title, message)
    }

    override fun showAwait(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showAwait(title, message)
    }

    override fun restartActivity() {
        (requireActivity() as? BaseActivity<*, *>)?.restartActivity()
    }

    override fun onLogout() {
        (requireActivity() as BaseActivity<*, *>).onLogout()
    }

    override fun showLoading() {
        (requireActivity() as BaseActivity<*, *>).showLoading()
    }

    override fun hideLoading() {
        (requireActivity() as BaseActivity<*, *>).hideLoading()
    }

    override fun navigateBack() {
        requireRouter().exit()
    }

    protected fun showExistInventory() {
        mViewModel.getSavedInventoryInfo().apply {
            showExistInventoryDialog(
                    this["name"] as String?,
                    this["desc"] as String?,
                    this["imgRes"] as Int?
            )
        }
    }

    private fun showExistInventoryDialog(title: String?, description: String?, imgRes: Int? = null) {
        val actionDialog = InventoryExistDialog.Builder()
                .setCancelable(true)
                .setMainTitle(getString(R.string.not_ended_inventory))
                .setTitle(title ?: "")
                .setDescription(description ?: "")
                .setImage(imgRes ?: R.drawable.ic_tractor)
                .setOnConfirmCallback(object : InventoryExistDialog.OnInventoryExistCallback {
                    override fun onContinueClicked() {
                        val inventory = mViewModel.getSavedInventory()!!
                        if (inventory.type == InventoryType.OFFICE) {
                            requireRouter().navigateTo(
                                    TransferConfirmFragment.getScreen(
                                            Gson().fromJson(
                                                    inventory.body,
                                                    OfficeInventory::class.java
                                            )
                                    )
                            )
                        } else if (inventory.type == InventoryType.TRANSPORT) {
                            requireRouter().navigateTo(
                                    kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmFragment.getScreen(
                                            Gson().fromJson(
                                                    inventory.body,
                                                    TransportInventory::class.java
                                            )
                                    )
                            )
                        } else if (inventory.type == InventoryType.WAREHOUSE) {
                            requireRouter().replaceScreen(
                                    kz.das.dasaccounting.ui.warehouse.transfer.TransferConfirmFragment.getScreen(
                                            Gson().fromJson(
                                                    inventory.body,
                                                    WarehouseInventory::class.java
                                            ), arrayListOf()
                                    )
                            )
                        } else {

                            //Fligger
                        }
                    }

                    override fun onCancelClicked() {
                        requireRouter().navigateTo(DeleteSavedInventoryFragment.getScreen())
                    }
                }).build()
        actionDialog.show(childFragmentManager, ActionInventoryConfirmDialog.TAG)
    }
    
}
