package kz.das.dasaccounting.core.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.banners.Banner
import kz.das.dasaccounting.core.ui.dialogs.NotificationDialog
import kz.das.dasaccounting.core.ui.dialogs.ProgressDialogFragment
import kz.das.dasaccounting.core.ui.shared.NetworkConnectionVM
import kz.das.dasaccounting.core.ui.utils.NetworkUtils
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BaseActivity<VM: ViewModel, VB: ViewBinding>: AppCompatActivity() {

    private val mNetworkConnectionVM: NetworkConnectionVM by viewModel()

    //region vars
    protected lateinit var mViewBinding: VB
    protected abstract val mViewModel: VM

    private var dialogLoading: ProgressDialogFragment? = null
    private var commonNotificationDialog: NotificationDialog? = null
    //endregion

    protected abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        dialogLoading = ProgressDialogFragment()
        mViewBinding = getViewBinding()
        super.onCreate(savedInstanceState)

        handleNetworkState()
    }

    fun showConnectionError() {
        showError(
                getString(R.string.common_error_no_connection),
                getString(R.string.common_error_could_not_connect_to_server)
        )
    }

    fun showNetworkResponseError(message: String?) {
        showError(getString(R.string.common_error_of_server), message ?: "")
    }

    fun showNullResponseError() {
        showError(getString(R.string.common_error_of_server), getString(R.string.common_no_data))
    }

    fun showBackendResponseError(backendErrorCode: Int?, message: String?) {
        val title = when (backendErrorCode) {
            in 500..599 -> getString(R.string.common_error_of_server)
            else -> getString(R.string.common_error)
        }
        showError(title, message)
    }

    fun showUnknownError(message: String?) {
        showError(getString(R.string.common_unexpected_error), message ?: "")
    }

    fun showUiError(message: String?) {
        showSimpleBanner(getString(R.string.common_error), message, Banner.Type.ERROR)
    }

    fun showError(title: String?, message: String?) {
        showSimpleBanner(title, message, Banner.Type.ERROR)
    }

    fun showSuccess(title: String?, message: String?) {
        showSimpleBanner(title, message, Banner.Type.SUCCESS)
    }

    fun showAwait(title: String?, message: String?) {
        showSimpleBanner(title, message, Banner.Type.AWAIT)
    }

    fun saveDataRequired(title: String?, message: String?, data: Any?) {

    }

    open fun restartActivity() {
        finish()
        startActivity(intent)
    }

    open fun onLogout() { }

    fun showLoading() {
        if (dialogLoading?.isAdded != true) {
            dialogLoading?.show(supportFragmentManager, ProgressDialogFragment.TAG)
        }
    }

    fun hideLoading() {
        if (dialogLoading?.isAdded == true) {
            dialogLoading?.dismiss()
        }
    }

    fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = ContextCompat.getColor(this@BaseActivity, color)
            }
        }
    }

    private fun showSimpleBanner(title: String?, message: String?, type: Banner.Type) {
        Banner.Builder(this)
                .setTitle(title)
                .setText(message)
                .setType(type)
                .build()
                .show(this)
    }


    private fun handleNetworkState() {
        NetworkUtils.getNetworkLiveData(this)
            .observe(this, Observer { isAvailable ->
                isAvailable?.let {
                    if (isAvailable) {
                        mNetworkConnectionVM.setResult(true)
                    } else {
                        mNetworkConnectionVM.setResult(false)
                        showInternetConnectionDialog()
                    }
                }
            })
    }

    private fun showInternetConnectionDialog() {
        commonNotificationDialog = NotificationDialog.Builder()
            .setTitle(getString(R.string.common_error_no_connection))
            .setDescription(getString(R.string.common_error_no_internet_connection))
            .setButtonText(getString(R.string.common_continue))
            .build()
        commonNotificationDialog?.show(
            supportFragmentManager,
            "NetworkConnectionNotificationDialog")
    }

}