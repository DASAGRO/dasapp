package kz.das.dasaccounting.ui.office.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.parent_bottom.profile.support.DialogBottomMediaTypePick
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportAttachedMediaAdapter
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptConfirmationFragment : BaseFragment<AcceptConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        private const val OFFICE_INVENTORY = "inventory"

        fun getScreen(officeInventoryAccept: OfficeInventory) = DasAppScreen(AcceptConfirmationFragment()).apply {
            val args = Bundle()
            args.putParcelable(OFFICE_INVENTORY, officeInventoryAccept)
            this.setArgs(args)
        }
    }

    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    override val mViewModel: AcceptConfirmationVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewModel.setOfficeInventory(getOfficeInventory())

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
                mViewModel.acceptInventory(mViewBinding.edtComment.text.toString() ?: "")
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            initViews(it)
        })

        mViewModel.isOfficeInventoryAccepted().observe(viewLifecycleOwner, Observer {
            if (it) {
                showSuccess(getString(R.string.common_banner_success),
                    getString(R.string.office_inventory_accepted_successfully))
                Screens.getRoleScreens(mViewModel.getUserRole() ?: "")?.let { screen ->
                    requireRouter().newRootScreen(screen)
                }
            }
        })

        mViewModel.isFilesUploading().observe(viewLifecycleOwner, Observer {
            if (it) {
                showUploading()
            } else {
                hideUploading()
            }
        })

        mViewModel.isOnAwait().observe(viewLifecycleOwner, Observer {
            if (it) {
                showAwait(getString(R.string.common_banner_await), "Получение ТМЦ в ожидании!")
                Screens.getRoleScreens(mViewModel.getUserRole() ?: "")?.let { screen ->
                    requireRouter().newRootScreen(screen)
                }
            }
        })
    }

    private fun initViews(officeInventory: OfficeInventory?) {
        officeInventory?.let {
            mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
            mViewBinding.tvInventoryTitle.text = it.name
            mViewBinding.tvInventoryDesc.text =
                (getString(R.string.inventory_total_quantity) +
                        " " + it.quantity +
                        " " + it.type + "\n" +
                        String.format(getString(R.string.inventory_sender_name), it.senderName))
        }
    }

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }
}