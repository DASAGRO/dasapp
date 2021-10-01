package kz.das.dasaccounting.ui.warehouse.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.ui.parent_bottom.profile.support.DialogBottomMediaTypePick
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportAttachedMediaAdapter
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kz.das.dasaccounting.ui.utils.MediaPlayerUtils
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptConfirmationFragment: BaseFragment<AcceptConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        private const val WAREHOUSE = "inventory"

        fun getScreen(warehouse: WarehouseInventory) = DasAppScreen(AcceptConfirmationFragment()).apply {
            val args = Bundle()
            args.putParcelable(WAREHOUSE, warehouse)
            this.setArgs(args)
        }
    }

    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    override val mViewModel: AcceptConfirmationVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setWarehouseInventory(getTransportInventory())

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

            btnConfirm.setOnClickListener {
                mViewModel.acceptInventory(mViewBinding.edtComment.text.toString() ?: "")
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getWarehouseInventory().observe(viewLifecycleOwner, Observer {
            initViews(it)
        })

        mViewModel.isWarehouseInventoryAccepted().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success), "Склад успешно принят")
                MediaPlayerUtils.playSuccessSound(requireContext())
                requireRouter().exit()
            }
        })

        mViewModel.isFilesUploading().observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

    }

    private fun initViews(officeInventory: WarehouseInventory?) {
        officeInventory?.let {
            mViewBinding.ivInventory.setImageResource(R.drawable.ic_warehouse)
            mViewBinding.tvInventoryTitle.text = it.name
            mViewBinding.tvInventoryDesc.text =
                (getString(R.string.seal_number) +
                        " " + it.sealNumber)
        }
    }

    private fun getTransportInventory(): WarehouseInventory? {
        return arguments?.getParcelable(WAREHOUSE)
    }
}