package kz.das.dasaccounting.ui.parent_bottom.profile

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_about_app.*
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentAboutAppBinding
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class AboutAppFragment : BaseFragment<ProfileVM, FragmentAboutAppBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(AboutAppFragment())
    }

    override val mViewModel: ProfileVM by viewModel()

    override fun getViewBinding() = FragmentAboutAppBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.run {
            tvVersion.text = "Version ${context?.packageManager?.getPackageInfo(context?.packageName!!, 0)!!.versionName}"

            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
        }
    }

    override fun onDestroy() {
        showBottomNavMenu()
        super.onDestroy()
    }
}