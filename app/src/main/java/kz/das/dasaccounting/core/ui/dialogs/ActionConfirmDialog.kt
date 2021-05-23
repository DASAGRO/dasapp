package kz.das.dasaccounting.core.ui.dialogs

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.R
import kz.das.dasaccounting.databinding.DialogActionConfirmBinding


private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val CANCELABLE = "cancelable"

class ActionConfirmDialog : BaseDialogFragment<DialogActionConfirmBinding>() {

    private var mListener: OnConfirmCallback? = null

    companion object {
        const val TAG = "NotificationDialog"

        fun newInstance() = NotificationDialog()
    }

    interface OnConfirmCallback {
        fun onConfirmClicked()
        fun onCancelClicked()
    }

    private fun setOnConfirmCallback(mListener: OnConfirmCallback?) {
        this.mListener = mListener
    }

    override fun getViewBinding() = DialogActionConfirmBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun setupUI() {
        mViewBinding.btnConfirm.setOnClickListener {
            mListener?.onConfirmClicked()
            dismiss()
        }
        mViewBinding.btnCancel.setOnClickListener {
            mListener?.onCancelClicked()
            dismiss()
        }
        when (val title = arguments?.getCharSequence(TITLE)) {
            null -> mViewBinding.tvTitle.visibility = View.GONE
            else -> mViewBinding.tvTitle.text = title
        }
        when (val description = arguments?.getCharSequence(DESCRIPTION)) {
            null -> mViewBinding.tvDescription.visibility = View.GONE
            else -> mViewBinding.tvDescription.text = description
        }
        isCancelable = arguments?.getBoolean(CANCELABLE) ?: true
    }

    class Builder {
        private var title: CharSequence? = null
        private var description: CharSequence? = null
        private var clickListener: OnConfirmCallback? = null
        private var cancelable: Boolean = true

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnConfirmCallback(clickListener: OnConfirmCallback): Builder {
            this.clickListener = clickListener
            return this
        }

        fun build(): ActionConfirmDialog {
            val dialog = ActionConfirmDialog()
            val args = Bundle()
            args.putCharSequence(TITLE, title)
            args.putCharSequence(DESCRIPTION, description)
            args.putBoolean(CANCELABLE, cancelable)
            dialog.setOnConfirmCallback(clickListener)
            dialog.arguments = args
            return dialog
        }
    }
}