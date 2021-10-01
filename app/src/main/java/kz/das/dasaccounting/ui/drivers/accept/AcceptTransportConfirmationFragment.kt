package kz.das.dasaccounting.ui.drivers.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptConfirmationBinding
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import kz.das.dasaccounting.ui.parent_bottom.profile.support.DialogBottomMediaTypePick
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportAttachedMediaAdapter
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kz.das.dasaccounting.ui.utils.MediaPlayerUtils
import kz.das.dasaccounting.utils.AppConstants
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptTransportConfirmationFragment : BaseFragment<AcceptTransportConfirmationVM, FragmentInventoryAcceptConfirmationBinding>() {

    companion object {
        private const val TRANSPORT_INVENTORY = "inventory"

        fun getScreen(transportInventoryAccept: TransportInventory) = DasAppScreen(AcceptTransportConfirmationFragment()).apply {
            val args = Bundle()
            args.putParcelable(TRANSPORT_INVENTORY, transportInventoryAccept)
            this.setArgs(args)
        }
    }

    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    override val mViewModel: AcceptTransportConfirmationVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptConfirmationBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setTransportInventory(getTransportInventory())

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

        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            initViews(it)
        })

        mViewModel.isTransportInventoryAccepted().observe(viewLifecycleOwner, Observer {
            if (it) {
                getTransportInventory()?.let {
                    requireRouter().replaceScreen(AcceptTransportCheckFragment.getScreen(it))
                }
//                showSuccess(getString(R.string.common_banner_success),
//                    if (mViewModel.getTransportInventory().value?.tsType.toString() == TransportType.TRAILED.type) {
//                        getString(R.string.transport_accessory_inventory_accepted_successfully)
//                    } else {
//                        getString(R.string.transport_inventory_accepted_successfully)
//                    }
//                )
//                MediaPlayerUtils.playSuccessSound(requireContext())
//                Screens.getRoleScreens(mViewModel.getUserRole() ?: "")?.let { screen ->
//                    requireRouter().newRootScreen(screen)
//                }
            }
        })

        mViewModel.isFilesUploading().observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        mViewModel.isReportSent().observe(viewLifecycleOwner, Observer {
            if (it) {
                hideLoading()
                getTransportInventory()?.let {
                    requireRouter().replaceScreen(AcceptTransportCheckFragment.getScreen(it, AppConstants.IS_SUCCESS))
                }
//                showSuccess(getString(R.string.common_banner_success), "Отчет успешно отправлен!")
//                Screens.getRoleScreens(mViewModel.getUserRole() ?: "")?.let { screen ->
//                    requireRouter().newRootScreen(screen)
//                }
            }
        })

        mViewModel.isOnAwait().observe(viewLifecycleOwner, Observer {
            if (it) {
                getTransportInventory()?.let {
                    requireRouter().replaceScreen(AcceptTransportCheckFragment.getScreen(it, AppConstants.IS_ON_AWAIT))
                }
//                showAwait(getString(R.string.common_banner_await),
//                    if (mViewModel.getTransportInventory().value?.tsType.toString() == TransportType.TRAILED.type) {
//                        "Получение ПО в ожидании!"
//                    } else {
//                        "Получение ТС в ожидании!"
//                    }
//                )
//                MediaPlayerUtils.playSuccessSound(requireContext())
//                Screens.getRoleScreens(mViewModel.getUserRole() ?: "")?.let { screen ->
//                    requireRouter().newRootScreen(screen)
//                }
            }
        })

    }

    private fun initViews(officeInventory: TransportInventory?) {
        officeInventory?.let {
            mViewBinding.ivInventory.setTsTypeImage(officeInventory)
            mViewBinding.tvInventoryTitle.text = it.model
            mViewBinding.tvInventoryDesc.text =
                (getString(R.string.gov_number) +
                        " " + it.stateNumber)
        }
    }

    private fun getTransportInventory(): TransportInventory? {
        return arguments?.getParcelable(TRANSPORT_INVENTORY)
    }
}