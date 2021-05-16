package kz.das.dasaccounting.core.ui.dialogs

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kz.das.dasaccounting.R
import kz.das.dasaccounting.databinding.DialogNotificationBinding


private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val BUTTON_COLOR_ID = "colorId"
private const val BUTTON_TEXT = "buttonText"
private const val CANCELABLE = "cancelable"

class NotificationDialog : BaseDialogFragment<DialogNotificationBinding>() {

    private var mListener: OnConfirmCallback? = null

    companion object {
        const val TAG = "NotificationDialog"

        fun newInstance() = NotificationDialog()
    }

    interface OnConfirmCallback {
        fun onConfirmClicked()
    }

    private fun setOnConfirmCallback(mListener: OnConfirmCallback?) {
        this.mListener = mListener
    }

    override fun getViewBinding() = DialogNotificationBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun setupUI() {
        mViewBinding.btnConfirm.setOnClickListener {
            mListener?.onConfirmClicked()
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
        when (val buttonText = arguments?.getCharSequence(BUTTON_TEXT)) {
            null -> mViewBinding.btnConfirm.text = getString(R.string.common_continue)
            else -> mViewBinding.btnConfirm.text = buttonText
        }
        mViewBinding.btnConfirm.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), arguments?.getInt(
                BUTTON_COLOR_ID
            ) ?: R.color.purple_700))

        isCancelable = arguments?.getBoolean(CANCELABLE) ?: true
    }

    class Builder {
        private var title: CharSequence? = null
        private var description: CharSequence? = null
        private var buttonText: CharSequence? = null
        private var colorId: Int = R.color.purple_700
        private var cancelable: Boolean = true
        private var clickListener: OnConfirmCallback? = null

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        fun setButtonColor(colorId: Int): Builder {
            this.colorId = colorId
            return this
        }

        fun setButtonText(buttonText: CharSequence): Builder {
            this.buttonText = buttonText
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

        fun build(): NotificationDialog {
            val dialog = NotificationDialog()
            val args = Bundle()
            args.putCharSequence(TITLE, title)
            args.putCharSequence(DESCRIPTION, description)
            args.putCharSequence(BUTTON_TEXT, buttonText)
            args.putInt(BUTTON_COLOR_ID, colorId)
            args.putBoolean(CANCELABLE, cancelable)
            dialog.setOnConfirmCallback(clickListener)
            dialog.arguments = args
            return dialog
        }
    }
}