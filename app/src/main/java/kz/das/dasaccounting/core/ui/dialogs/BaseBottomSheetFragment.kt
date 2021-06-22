package kz.das.dasaccounting.core.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.activities.BaseActivity
import kz.das.dasaccounting.core.ui.fragments.ViewCallback
import kz.das.dasaccounting.core.ui.view_model.BaseVM

abstract class BaseBottomSheetFragment<VB : ViewBinding, VM : BaseVM> :
        BottomSheetDialogFragment(),
        ViewCallback {

    //region vars
    protected lateinit var mViewBinding: VB
    //endregion

    //region abstract fields
    protected abstract val mViewModel: VM
    //endregion

    //region abstract methods
    protected abstract fun getViewBinding(): VB
    protected abstract fun setupUI()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewBinding = getViewBinding()
        setupUI()
        return mViewBinding.root
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
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

            //region errors
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
            //endregion
        }
    }

    override fun showSuccess(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showSuccess(title, message)
    }

    override fun showError(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showError(title, message)
    }

    override fun restartActivity() {
        requireActivity().apply {
            finish()
            startActivity(intent)
        }
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

    override fun showUploading() {
        (requireActivity() as BaseActivity<*, *>).showUploading()
    }

    override fun hideUploading() {
        (requireActivity() as BaseActivity<*, *>).hideUploading()
    }

    override fun navigateBack() {
        requireRouter().exit()
    }
}


abstract class BaseBottomSheetDialog<VB : ViewBinding> : BottomSheetDialogFragment(),
    ViewCallback {

    //region vars
    protected lateinit var mViewBinding: VB
    //endregion

    //region abstract methods
    protected abstract fun getViewBinding(): VB
    protected abstract fun setupUI()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewBinding = getViewBinding()
        setupUI()
        return mViewBinding.root
    }

    override fun onStart() {
        super.onStart()
        observeLiveData()
    }

    override fun showSuccess(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showSuccess(title, message)
    }

    override fun showError(title: String?, message: String?) {
        (requireActivity() as? BaseActivity<*, *>)?.showError(title, message)
    }

    override fun restartActivity() {
        requireActivity().apply {
            finish()
            startActivity(intent)
        }
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

    override fun showUploading() {
        (requireActivity() as BaseActivity<*, *>).showUploading()
    }

    override fun hideUploading() {
        (requireActivity() as BaseActivity<*, *>).hideUploading()
    }

    override fun navigateBack() {
        requireRouter().exit()
    }
}