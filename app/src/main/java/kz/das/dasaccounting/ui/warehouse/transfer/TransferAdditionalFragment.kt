package kz.das.dasaccounting.ui.warehouse.transfer

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.core.ui.dialogs.BaseBottomSheetFragment
import kz.das.dasaccounting.databinding.FragmentBottomSheetWarehouseTransferBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportAttachedMediaAdapter
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import org.koin.android.viewmodel.ext.android.viewModel

class TransferAdditionalFragment: BaseBottomSheetFragment<FragmentBottomSheetWarehouseTransferBinding, TransferAdditionalVM>() {

    private var onTransferFieldsListener: OnTransferFieldsListener? = null
    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    companion object {
        private const val WAREHOUSE = "warehouse"
        fun newInstance(warehouse: WarehouseInventory?) = TransferAdditionalFragment().apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE, warehouse)
            this.arguments = args
        }
    }

    interface OnTransferFieldsListener {
        fun onTransfer(warehouse: WarehouseInventory?, fileIds: ArrayList<Int>)
    }

    fun setOnTransferFieldsListener(onTransferFieldsListener: OnTransferFieldsListener) {
        this.onTransferFieldsListener =  onTransferFieldsListener
    }

    override val mViewModel: TransferAdditionalVM by viewModel()

    override fun getViewBinding() = FragmentBottomSheetWarehouseTransferBinding.inflate(layoutInflater)

    override fun setupUI() {

        mViewModel.setWarehouseInventory(getWarehouse())

        profileSupportAttachedMediaAdapter = ProfileSupportAttachedMediaAdapter(requireContext(),
            object : ProfileSupportAttachedMediaAdapter.ProfileSupportAttachedMediaAdapterEvents {
                override fun onRemoveMedia(media: Media, position: Int) {
                    profileSupportAttachedMediaAdapter?.removeItem(media)
                    mViewModel.remove(position)
                }
            }, arrayListOf()
        )

        mViewBinding.apply {

            rvMedia.adapter = profileSupportAttachedMediaAdapter

            ivClose.setOnClickListener {
                dismiss()
            }
            edtSealNumber.addTextChangedListener {
                btnTransfer.isEnabled = !it.isNullOrEmpty()
            }
            tvWarehouseNaming.text = getWarehouse()?.name

            btnUploadVideo.setOnClickListener {
                TedImagePicker.with(requireContext())
                    .backButton(R.drawable.ic_arrow_back)
                    .title("Выбрать видео")
                    .mediaType(MediaType.VIDEO)
                    .buttonBackground(R.drawable.selectable_default_button_background)
                    .zoomIndicator(true)
                    .max(5, "Максимум 5 медиафайлов")
                    .imageCountTextFormat("Выбрано %s")
                    .buttonText("Готово")
                    .cameraTileBackground(R.color.white)
                    .startMultiImage { list ->
                        profileSupportAttachedMediaAdapter?.addList(list.mapIndexed { index, uri -> Media(index, uri) })
                        mViewModel.upload(list)
                    }
            }

            btnScanQr.setOnClickListener {
                val qrFragment = QrFragment.Builder()
                    .setCancelable(true)
                    .setOnScanCallback(object : QrFragment.OnScanCallback {
                        override fun onScan(qrScan: String) {
                            delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                                edtSealNumber.setText(qrScan)
                            }
                        }
                    }).build()
                qrFragment.show(parentFragmentManager, "QrAcceptFragment")
            }

            btnTransfer.setOnClickListener {
                if (edtSealNumber.text.isNullOrEmpty()) {
                    showError(getString(R.string.common_error), "Введите номер пломбы!")
                } else {
                    val warehouseInventory = mViewModel.getWarehouseInventory()
                    warehouseInventory?.sealNumber = edtSealNumber.text.toString()
                    warehouseInventory?.date = System.currentTimeMillis()
                    warehouseInventory?.latitude = mViewModel.getUserLocation().lat
                    warehouseInventory?.longitude = mViewModel.getUserLocation().long
                    onTransferFieldsListener?.onTransfer(warehouseInventory, mViewModel.getFileIds())
                    dismiss()
                }
            }
        }
    }

    override fun showAwait(title: String?, message: String?) { }

    override fun onSaveRequire(title: String?, message: String?, data: Any?) { }

    private fun getWarehouse(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE)
    }

}