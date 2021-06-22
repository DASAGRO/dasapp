package kz.das.dasaccounting.ui.parent_bottom.profile.support

import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetDialog
import kz.das.dasaccounting.databinding.DialogBottomSheetMediaTypePickBinding


class DialogBottomMediaTypePick: BaseBottomSheetDialog<DialogBottomSheetMediaTypePickBinding>() {

    companion object {
        fun newInstance() = DialogBottomMediaTypePick()
    }

    override fun getViewBinding() = DialogBottomSheetMediaTypePickBinding.inflate(layoutInflater)

    private var listener: OnMediaTypeListener? = null

    interface OnMediaTypeListener {
        fun onMediaImagePicked()
        fun onMediaVideoPicked()
    }

    fun setListener(listener: OnMediaTypeListener) {
        this.listener = listener
    }

    override fun setupUI() {
        mViewBinding.btnMediaImage.setOnClickListener {
            listener?.onMediaImagePicked()
            dismiss()
        }
        mViewBinding.btnMediaVideo.setOnClickListener {
            listener?.onMediaVideoPicked()
            dismiss()
        }
    }

    override fun observeLiveData() { }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }
}
