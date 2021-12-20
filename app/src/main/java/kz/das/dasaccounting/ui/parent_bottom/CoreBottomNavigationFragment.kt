package kz.das.dasaccounting.ui.parent_bottom

import android.Manifest
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.dialogs.NotificationDialog
import kz.das.dasaccounting.core.ui.extensions.*
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentNavBarParentBinding
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.container.ContainerFragment
import kz.das.dasaccounting.ui.utils.CameraUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

open class CoreBottomNavigationFragment : BaseFragment<CoreBottomNavigationVM, FragmentNavBarParentBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(CoreBottomNavigationFragment())
    }

    final override val mViewModel: CoreBottomNavigationVM by sharedViewModel()

    //location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    override fun getViewBinding() = FragmentNavBarParentBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationTracking()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            showFragment(Screens.ScreenLinks.location.toString())
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        //changeStatusColor(R.color.teal_200)

        if (!CameraUtils.isPermissionGranted(requireContext())) {
            showCameraPermissionRationaleDialog()
        }
        requestLocation()

        mViewBinding.bslOperations.isVisible = mViewModel.isOnWork()

        mViewBinding.bottomNavigationView.background = null

        mViewBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (mViewModel.isOnWork()) mViewBinding.bslOperations.isVisible = true
                    showFragment(Screens.ScreenLinks.location.toString())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    if (mViewModel.isOnWork()) mViewBinding.bslOperations.isVisible = false
                    showFragment(Screens.ScreenLinks.profile.toString())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.isControlOptionsShow().observe(viewLifecycleOwner, Observer {
            mViewBinding.bslOperations.isVisible = it
        })
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
                        Screens.ScreenLinks.profile.toString()
                    )
                }
                else -> {
                    ContainerFragment.newInstance(
                        Screens.ScreenLinks.location.toString()
                    )
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
        if (mViewBinding.bottomAppBar.visibility == View.GONE) {
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

    // LOCATION TRACKING
    private fun initLocationTracking() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.SECONDS.toMillis(10)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.lastLocation
                currentLocation?.let {
                    mViewModel.saveLocation(it.longitude, it.latitude)
                }
                Log.d("####", "currentLocation: $currentLocation")
            }
        }
    }

    private fun requestLocation() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (unlikely: SecurityException) {
            //locationIsTracking = false
            Log.d(
                "Location excp: ",
                "Lost location permissions. Couldn't remove updates. $unlikely"
            )
        }
    }

    open val requestCameraPermissionsLaunch =
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
        val notificationDialog = NotificationDialog.Builder()
            .setDescription(getString(R.string.camera_qr_scan_permission_required))
            .setTitle(getString(R.string.camera_access_title))
            .setCancelable(true).build()
        notificationDialog.show(childFragmentManager, "CameraPermissionNotificationDialog")
    }

    private fun showCameraPermissionRationaleDialog() {
        val notificationDialog = NotificationDialog.Builder()
            .setDescription(getString(R.string.camera_qr_scan_permission_required_rationale))
            .setTitle(getString(R.string.camera_access_title))
            .setOnConfirmCallback(object : NotificationDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    requestCameraPermissionsLaunch.launch(Manifest.permission.CAMERA)
                }
            })
            .setCancelable(true).build()
        notificationDialog.show(childFragmentManager, "CameraPermissionNotificationDialog")
    }

    private fun hideBottomOptions() {
        if (mViewModel.isOnWork()) {
            mViewBinding.bslOperations.collapse()
        }
    }

    private fun showBottomOptions() {
        if (mViewModel.isOnWork()) {
            mViewBinding.bslOperations.expand()
        }
    }
}

fun Fragment.hideBottomNavMenu() {
    if (parentFragment?.parentFragment is CoreBottomNavigationFragment) {
        (parentFragment?.parentFragment as CoreBottomNavigationFragment).hideBottomMenu()
    }
}

fun Fragment.showBottomNavMenu() {
    if (parentFragment?.parentFragment is CoreBottomNavigationFragment) {
        (parentFragment?.parentFragment as CoreBottomNavigationFragment).showBottomMenu()
    }
}