package kz.das.dasaccounting.ui.drivers.fligel

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.ui.parent_bottom.profile.support.DialogBottomMediaTypePick
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportAttachedMediaAdapter
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import org.koin.android.viewmodel.ext.android.viewModel

class TransferConfirmFligelDataFragment : BaseFragment<TransferConfirmFligelDataVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        private const val FLIGEL_DATA_INVENTORY = "fligel_data"

        fun getScreen(transportInventoryAccept: FligelProduct) = DasAppScreen(TransferConfirmFligelDataFragment()).apply {
            val args = Bundle()
            args.putParcelable(FLIGEL_DATA_INVENTORY, transportInventoryAccept)
            this.setArgs(args)
        }
    }

    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    override val mViewModel: TransferConfirmFligelDataVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getTransportInventory())

        profileSupportAttachedMediaAdapter = ProfileSupportAttachedMediaAdapter(requireContext(),
            object : ProfileSupportAttachedMediaAdapter.ProfileSupportAttachedMediaAdapterEvents {
                override fun onRemoveMedia(media: Media, position: Int) {
                    profileSupportAttachedMediaAdapter?.removeItem(media)
                    mViewModel.remove(position)
                }
            }, arrayListOf()
        )

        mViewBinding.apply {

            rvMedia.run {
                adapter = profileSupportAttachedMediaAdapter
            }

            tvAddMedia.setOnClickListener {
                val dialogBottomMediaTypePick = DialogBottomMediaTypePick.newInstance()
                dialogBottomMediaTypePick.setListener(object : DialogBottomMediaTypePick.OnMediaTypeListener {
                    override fun onMediaImagePicked() {
                        TedImagePicker.with(requireActivity())
                            .backButton(R.drawable.ic_arrow_back)
                            .title("Выбрать фото")
                            .mediaType(MediaType.IMAGE)
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

                    override fun onMediaVideoPicked() {
                        TedImagePicker.with(requireActivity())
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
                })
                dialogBottomMediaTypePick.show(childFragmentManager, dialogBottomMediaTypePick.tag)
            }

            btnReady.setOnClickListener {
                if (mViewBinding.edtComment.text.isNullOrEmpty()) {
                    showError(getString(R.string.common_error), "Введите комментарий для отправки")
                } else {
                    mViewModel.acceptInventory(mViewBinding.edtComment.text.toString() ?: "")
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getFligelData().observe(viewLifecycleOwner, Observer {
            initViews(it)
        })

        mViewModel.isTransportInventoryAccepted().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success),
                    getString(R.string.transport_inventory_accepted_successfully))
                requireRouter().exit()
            }
        })

        mViewModel.isFilesUploading().observe(viewLifecycleOwner, Observer {
            if (it) {
                showUploading()
            } else {
                hideUploading()
            }
        })
    }

    private fun initViews(fligel: FligelProduct?) {
        fligel?.let {
            mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
            mViewBinding.tvInventoryTitle.text = ""
            mViewBinding.tvInventoryDesc.text = (
                    "Номер комбайна: " + it.combinerNumber + "\n" +
                            "Номер поля: " + it.fieldNumber + "\n" +
                            "Вес урожая: " + it.harvestWeight + "\n" +
                            "Влажность: " + it.humidity)
        }
    }

    private fun getTransportInventory(): FligelProduct? {
        return arguments?.getParcelable(FLIGEL_DATA_INVENTORY)
    }
}