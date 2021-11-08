package kz.das.dasaccounting.core.ui.dialogs

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.R
import kz.das.dasaccounting.databinding.DialogInventoryExistBinding

private const val MAIN_TITLE = "main_title"
private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val IMAGE_RES_ID = "image"
private const val CANCELABLE = "cancelable"

class InventoryExistDialog : BaseDialogFragment<DialogInventoryExistBinding>() {
    private var mListener: OnInventoryExistCallback? = null

    companion object {
        const val TAG = "InventoryExistDialog"

        fun newInstance() = InventoryExistDialog()
    }

    interface OnInventoryExistCallback {
        fun onContinueClicked()
        fun onCancelClicked()
    }

    private fun setOnInventoryExistCallback(mListener: OnInventoryExistCallback?) {
        this.mListener = mListener
    }

    override fun getViewBinding() = DialogInventoryExistBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun setupUI() {
        mViewBinding.apply {

            when (val title = arguments?.getCharSequence(TITLE)) {
                null -> mViewBinding.tvInventoryTitle.visibility = View.GONE
                else -> mViewBinding.tvInventoryTitle.text = title
            }
            when (val description = arguments?.getCharSequence(DESCRIPTION)) {
                null -> mViewBinding.tvInventoryDesc.visibility = View.GONE
                else -> mViewBinding.tvInventoryDesc.text = description
            }
            mViewBinding.ivInventory.setImageResource(arguments?.getInt(IMAGE_RES_ID) ?: R.drawable.ic_inventory)
            isCancelable = arguments?.getBoolean(CANCELABLE) ?: true

            btnCancel.setOnClickListener {
                mListener?.onCancelClicked()
                dismiss()
            }

            btnContinue.setOnClickListener {
                mListener?.onContinueClicked()
                dismiss()
            }
        }
    }

    class Builder {
        private var title: CharSequence? = null
        private var mainTitle: CharSequence? = null
        private var description: CharSequence? = null
        private var clickListener: OnInventoryExistCallback? = null
        private var imageResId: Int? = null
        private var cancelable: Boolean = true

        fun setMainTitle(mainTitle: CharSequence): Builder {
            this.mainTitle = mainTitle
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: CharSequence): Builder {
            this.description = description
            return this
        }

        fun setImage(imageResId: Int): Builder {
            this.imageResId = imageResId
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnConfirmCallback(clickListener: OnInventoryExistCallback): Builder {
            this.clickListener = clickListener
            return this
        }

        fun build(): InventoryExistDialog {
            val dialog = InventoryExistDialog()
            val args = Bundle()
            args.putCharSequence(TITLE, title)
            args.putCharSequence(MAIN_TITLE, mainTitle)
            args.putCharSequence(DESCRIPTION, description)
            args.putBoolean(CANCELABLE, cancelable)
            args.putInt(IMAGE_RES_ID, imageResId ?: R.drawable.ic_inventory)
            dialog.setOnInventoryExistCallback(clickListener)
            dialog.arguments = args
            return dialog
        }
    }
}