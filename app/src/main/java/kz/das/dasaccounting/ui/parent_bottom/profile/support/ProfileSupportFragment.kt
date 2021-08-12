package kz.das.dasaccounting.ui.parent_bottom.profile.support

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileSupportBinding
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileSupportFragment: BaseFragment<ProfileSupportVM, FragmentProfileSupportBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileSupportFragment())
    }

    private var profileSupportAttachedMediaAdapter: ProfileSupportAttachedMediaAdapter? = null

    override val mViewModel: ProfileSupportVM by viewModel()

    override fun getViewBinding() = FragmentProfileSupportBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {

        profileSupportAttachedMediaAdapter = ProfileSupportAttachedMediaAdapter(requireContext(),
            object : ProfileSupportAttachedMediaAdapter.ProfileSupportAttachedMediaAdapterEvents {
                override fun onRemoveMedia(media: Media, position: Int) {
                    profileSupportAttachedMediaAdapter?.removeItem(media)
                    mViewModel.remove(position)
                }
            }, arrayListOf()
        )

        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }
        mViewBinding.rvMedia.run {
            adapter = profileSupportAttachedMediaAdapter
        }
        mViewBinding.tvAddMedia.setOnClickListener {
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
                            profileSupportAttachedMediaAdapter?.addList(list.mapIndexed { index, uri -> Media(index, uri)})
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
                            profileSupportAttachedMediaAdapter?.addList(list.mapIndexed { index, uri -> Media(index, uri)})
                            mViewModel.upload(list)
                        }
                }
            })
            dialogBottomMediaTypePick.show(childFragmentManager, dialogBottomMediaTypePick.tag)
        }

        mViewBinding.btnSend.setOnClickListener {
            if (mViewBinding.edtComment.text.isNullOrEmpty()) {
                showError(getString(R.string.common_error), "Введите комментарий для отправки")
            } else {
                mViewModel.sendSupport(mViewBinding.edtComment.text.toString())
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isFilesUploading().observe(viewLifecycleOwner, Observer {
            if (it) {
                showUploading()
            } else {
                hideUploading()
            }
        })

        mViewModel.isReportSent().observe(viewLifecycleOwner, Observer {
            if (it) {
                hideLoading()
                showSuccess(getString(R.string.common_banner_success), "Отчет успешно отправлен!")
                requireRouter().exit()
            }
        })
    }


    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }

}