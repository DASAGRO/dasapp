package kz.das.dasaccounting.core.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import kz.das.dasaccounting.R

abstract class BaseFullDialogFragment<VB: ViewBinding> : DialogFragment(){

    //region vars
    protected lateinit var mViewBinding: VB
    //endregion

    //region abstract methods
    protected abstract fun getViewBinding(): VB
    protected abstract fun setupUI()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getTheme(): Int {
        return R.style.DialogFullScreenTheme
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        isCancelable = false
        mViewBinding = getViewBinding()

        setupUI()

        return mViewBinding.root
    }

    fun showSuccessBanner() {

    }

    fun showErrorBanner() {

    }
}