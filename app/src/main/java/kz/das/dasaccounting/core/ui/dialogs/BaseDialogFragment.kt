package kz.das.dasaccounting.core.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import kz.das.dasaccounting.R

abstract class BaseDialogFragment<VB: ViewBinding> : DialogFragment(){

    //region vars
    protected lateinit var mViewBinding: VB
    //endregion

    //region abstract methods
    protected abstract fun getViewBinding(): VB
    protected abstract fun setupUI()
    //endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewBinding = getViewBinding()
        setupUI()

        return mViewBinding.root
    }
}