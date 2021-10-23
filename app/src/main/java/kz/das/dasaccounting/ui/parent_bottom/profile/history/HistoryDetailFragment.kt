package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.generateQR
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.data.source.local.typeconvertors.DriverInventoryTypeConvertor
import kz.das.dasaccounting.databinding.FragmentProfileHistoryDetailBinding
import kz.das.dasaccounting.domain.data.history.HistoryEnum
import kz.das.dasaccounting.domain.data.history.OperationType
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryDetailFragment: BaseFragment<HistoryDetailVM, FragmentProfileHistoryDetailBinding>() {

    override val mViewModel: HistoryDetailVM by viewModel()

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val TYPE = "type"
        private const val STATUS = "status"
        private const val QR = "qr"

        fun getScreen(title: String?, description: String?,
                      type: String?, status: String?, qr: String?) = DasAppScreen(HistoryDetailFragment()).apply {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(DESCRIPTION, description)
            args.putString(TYPE, type)
            args.putString(STATUS, status)
            args.putString(QR, qr)
            this.setArgs(args)
        }
    }

    override fun getViewBinding() = FragmentProfileHistoryDetailBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }
            if (getStatus() == HistoryEnum.AWAIT.status) {
                ivHistoryState.setImageResource(R.drawable.ic_banner_await)
            }
            when (getType()) {
                OperationType.OFFICE.status -> {
                    ivInventory.setImageResource(R.drawable.ic_inventory)
                }
                OperationType.WAREHOUSE.status -> {
                    ivInventory.setImageResource(R.drawable.ic_warehouse)
                }
                OperationType.DRIVER_ACCESSORY.status -> {
                    ivInventory.setImageResource(R.drawable.ic_trailer)
                }
                OperationType.DRIVER.status -> {
                    ivInventory.setImageResource(R.drawable.ic_tractor)
                } else -> {
                    ivInventory.setImageResource(R.drawable.ic_inventory)
                }
            }
            this.ivBarcode.visibility = if (getQrString().isNullOrEmpty()) View.GONE else View.VISIBLE
            this.ivBarcode.setImageBitmap(getQrString()?.generateQR())

            tvInventoryTitle.text = getTitle()
            tvInventoryDesc.text = getDescription()
            tvInventoryTime.text = ""
        }
    }

    private fun getTitle(): String? {
        return arguments?.getString(TITLE)
    }
    private fun getDescription(): String? {
        return arguments?.getString(DESCRIPTION)
    }
    private fun getType(): String? {
        return arguments?.getString(TYPE)
    }
    private fun getStatus(): String? {
        return arguments?.getString(STATUS)
    }
    private fun getQrString(): String? {
        return arguments?.getString(QR)
    }

}