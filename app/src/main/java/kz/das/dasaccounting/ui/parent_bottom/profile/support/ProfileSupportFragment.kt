package kz.das.dasaccounting.ui.parent_bottom.profile.support

import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileSupportBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileSupportFragment: BaseFragment<ProfileSupportVM, FragmentProfileSupportBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileSupportFragment())
    }

    override val mViewModel: ProfileSupportVM by viewModel()

    override fun getViewBinding() = FragmentProfileSupportBinding.inflate(layoutInflater)

    override fun setupUI() {
        mViewBinding.toolbar.setNavigationOnClickListener { requireRouter().exit() }
        mViewBinding.tvAddMedia.setOnClickListener {
            TedImagePicker.with(requireActivity())
                .backButton(R.drawable.ic_arrow_back)
                .title("Выбрать фото и видео")
                .mediaType(MediaType.IMAGE)
                .mediaType(MediaType.VIDEO)
                .buttonBackground(R.drawable.selectable_default_button_background)
                .zoomIndicator(true)
                .max(5, "Максимум 5 медиафайлов")
                .imageCountTextFormat("Выбрано %s")
                .buttonText("Готово")
                .cameraTileBackground(R.color.white)
                .startMultiImage { list ->

                }
        }
    }



    override fun onDestroyView() {
        showBottomNavMenu()
        super.onDestroyView()
    }

}