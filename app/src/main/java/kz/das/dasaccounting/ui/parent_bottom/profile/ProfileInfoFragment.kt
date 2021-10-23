package kz.das.dasaccounting.ui.parent_bottom.profile

import android.os.Bundle
import androidx.lifecycle.Observer
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.setAvatar
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentProfileInfoBinding
import kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset.ProfilePassResetFragment
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import kz.das.dasaccounting.utils.AppConstants
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileInfoFragment: BaseFragment<ProfileInfoVM, FragmentProfileInfoBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ProfileInfoFragment())
    }

    override val mViewModel: ProfileInfoVM by viewModel()

    override fun getViewBinding() = FragmentProfileInfoBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {

        mViewBinding.run {
            lifecycleOwner = viewLifecycleOwner
            profileInfoVM = mViewModel
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            rlPassReset.setOnClickListener {
                requireRouter().navigateTo(ProfilePassResetFragment.getScreen(AppConstants.PASS_RESET))
            }
            ivChangeAvatar.setOnClickListener {
                TedImagePicker.with(requireActivity())
                    .backButton(R.drawable.ic_arrow_back)
                    .title("Выбрать фото")
                    .mediaType(MediaType.IMAGE)
                    .buttonBackground(R.drawable.selectable_default_button_background)
                    .zoomIndicator(true)
                    .imageCountTextFormat("Выбрано %s")
                    .buttonText("Готово")
                    .cameraTileBackground(R.color.white)
                    .start { uri ->
                        mViewModel.updateImagePath(uri)
                    }
            }
            ibShare.setOnClickListener { mViewModel.sendLogs() }
        }

    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.getProfileImageLV().observe(viewLifecycleOwner, Observer {

            it?.let {
                mViewBinding.ivAvatar.setAvatar(it)
            }
        })
    }

    override fun onDestroy() {
        showBottomNavMenu()
        super.onDestroy()
    }
}