package kz.das.dasaccounting.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.core.ui.view.collapse
import kz.das.dasaccounting.core.ui.view.expand
import kz.das.dasaccounting.core.ui.view.zoomAnimation
import kz.das.dasaccounting.databinding.FragmentNavBarParentBinding
import kz.das.dasaccounting.ui.container.ContainerFragment
import kz.das.dasaccounting.ui.qr.QrFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import org.koin.android.viewmodel.ext.android.viewModel

class ParentBottomNavigationFragment: BaseFragment<ParentBottomNavigationVM, FragmentNavBarParentBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(ParentBottomNavigationFragment())
    }

    override val mViewModel: ParentBottomNavigationVM by viewModel()

    override fun getViewBinding() = FragmentNavBarParentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) { showFragment(Screens.ScreenLinks.location.toString()) }
    }

    override fun setupUI() {
        changeStatusColor(R.color.teal_200)
        mViewBinding.fabQr.setOnClickListener {
            if (!CameraUtils.isPermissionGranted(requireContext())) {
                requestCameraPermissionsLaunch.launch(Manifest.permission.CAMERA)
            } else {
                showQr()
            }
        }
        mViewBinding.bottomNavigationView.background = null
        mViewBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home-> {
                    showFragment(Screens.ScreenLinks.location.toString())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    showFragment(Screens.ScreenLinks.profile.toString())
                    return@setOnNavigationItemSelectedListener true
                } else -> { return@setOnNavigationItemSelectedListener false }
            }
        }
    }

    private fun showFragment(tab: String) {
        val fm = childFragmentManager
        var tabFragment = fm.findFragmentByTag(tab)

        val isFragmentExist = tabFragment != null
        tabFragment = if (isFragmentExist) {
            tabFragment
        } else {
            when (tab) {
                Screens.ScreenLinks.profile.toString() -> {
                    ContainerFragment.newInstance(
                            Screens.ScreenLinks.profile.toString())
                }
                else -> {
                    ContainerFragment.newInstance(
                            Screens.ScreenLinks.location.toString())
                }
            }
        }

        hideFragments(fm, tab)

        if (isFragmentExist) {
            fm.beginTransaction().show(tabFragment!!).commit()
        } else {
            fm.beginTransaction().add(R.id.navContainer, tabFragment!!, tab)
                    .commit()
        }
    }

    private fun hideFragments(fm: FragmentManager, tag: String) {
        val locationFragment = fm.findFragmentByTag(Screens.ScreenLinks.location.toString())
        val profileFragment = fm.findFragmentByTag(Screens.ScreenLinks.profile.toString())

        if (tag != Screens.ScreenLinks.location.toString() && locationFragment != null && !locationFragment.isHidden) {
            fm.beginTransaction().hide(locationFragment).commit()
        } else if (tag != Screens.ScreenLinks.profile.toString() && profileFragment != null && !profileFragment.isHidden) {
            fm.beginTransaction().hide(profileFragment).commit()
        }
    }


    fun showBottomMenu() {
        if (mViewBinding.bottomAppBar.visibility ==  View.GONE) {
            mViewBinding.bottomAppBar.expand()
            mViewBinding.fabQr.zoomAnimation(300, true)
        }
    }

    fun hideBottomMenu() {
        if (mViewBinding.bottomAppBar.visibility == View.VISIBLE) {
            mViewBinding.bottomAppBar.collapse()
            mViewBinding.fabQr.zoomAnimation(300, false)
        }
    }

    private val requestCameraPermissionsLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                val provideRationale =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                    } else {
                        false
                    }

                if (provideRationale) {
                    showCameraPermissionRequireDialog()
                }
            }
        }


    private fun showCameraPermissionRequireDialog() {

    }

    private fun showQr() {
        val qrFragment = QrFragment.Builder()
            .setCancelable(true)
            .setOnScanCallback(object : QrFragment.OnScanCallback {
                override fun onScan(qrScan: String) { }
            })
            .build()
        qrFragment.show(childFragmentManager, "QrFragment")
    }
}


fun Fragment.hideBottomNavMenu() {
    if (parentFragment?.parentFragment is ParentBottomNavigationFragment) {
        (parentFragment?.parentFragment as ParentBottomNavigationFragment).hideBottomMenu()
    }
}

fun Fragment.showBottomNavMenu() {
    if (parentFragment?.parentFragment is ParentBottomNavigationFragment) {
        (parentFragment?.parentFragment as ParentBottomNavigationFragment).showBottomMenu()
    }
}