package kz.das.dasaccounting.ui.qr

import android.os.Bundle
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.banners.Banner
import kz.das.dasaccounting.core.ui.dialogs.*
import kz.das.dasaccounting.databinding.FragmentQrBinding

private const val TITLE = "title"
private const val CANCELABLE = "cancelable"

class QrFragment: BaseFullDialogFragment<FragmentQrBinding>() {

    private lateinit var codeScanner: CodeScanner

    private var mListener: OnScanCallback? = null

    override fun getViewBinding() = FragmentQrBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.tvQrTitle.text = arguments?.getCharSequence(TITLE) ?: getString(R.string.navigate_to_qr)
        mViewBinding.ibQr.setOnClickListener { dismiss() }
        isCancelable = arguments?.getBoolean(CANCELABLE) ?: true
        codeScanner = CodeScanner(requireContext(), mViewBinding.scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            mListener?.onScan(it.text ?: "")
            showQrDecodeResultBanner(it.text ?: "")
        }

        codeScanner.errorCallback = ErrorCallback {
            showQrDecodeErrorBanner()
        }

        mViewBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    interface OnScanCallback {
        fun onScan(qrScan: String)
    }

    private fun setOnConfirmCallback(mListener: OnScanCallback?) {
        this.mListener = mListener
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun showQrDecodeResultBanner(result: String?) {
        Banner.Builder(viewLifecycleOwner)
            .setTitle(getString(R.string.success_qr_scan))
            .setText(result)
            .setType(Banner.Type.SUCCESS)
            .build()
            .show(requireActivity())
    }

    private fun showQrDecodeErrorBanner() {
        Banner.Builder(viewLifecycleOwner)
            .setTitle(getString(R.string.error_scan))
            .setText(getString(R.string.error_type_qr))
            .setType(Banner.Type.ERROR)
            .build()
            .show(requireActivity())
    }

    class Builder {
        private var title: CharSequence? = null
        private var cancelable: Boolean = true
        private var mListener: OnScanCallback? = null

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setOnScanCallback(mListener: OnScanCallback): Builder {
            this.mListener = mListener
            return this
        }

        fun build(): QrFragment {
            val dialog = QrFragment()
            val args = Bundle()
            args.putCharSequence(TITLE, title)
            args.putBoolean(CANCELABLE, cancelable)
            dialog.setOnConfirmCallback(mListener)
            dialog.arguments = args
            return dialog
        }
    }

}