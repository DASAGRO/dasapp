package kz.das.dasaccounting.ui.parent_bottom.location

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.dialogs.NotificationDialog
import kz.das.dasaccounting.core.ui.extensions.zoomAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentLocationBinding
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationVM
import kz.das.dasaccounting.ui.utils.GeolocationUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LocationFragment: BaseFragment<LocationVM, FragmentLocationBinding>(), OnMapReadyCallback {

    companion object {
        fun getScreen() = DasAppScreen(LocationFragment())
    }

    private lateinit var mapboxMap: MapboxMap
    private var mapView: MapView? = null

    override val mViewModel: LocationVM by viewModel()
    private val coreMainVM: CoreBottomNavigationVM by sharedViewModel()


    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = mViewBinding.mapView
        mapView?.onCreate(savedInstanceState)

        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { }
        }
    }

    override fun setupUI() {
        mViewBinding.run {
            btnStart.isVisible = !coreMainVM.isOnWork()
            btnStop.isVisible = coreMainVM.isOnWork()

            btnStart.setOnClickListener {
                coreMainVM.startWork()
                coreMainVM.setControlOptionsState(true)
                btnStart.zoomAnimation(300L, false)
                btnStop.zoomAnimation(300L, true)
            }

            btnStop.setOnClickListener {
                coreMainVM.stopWork()
                coreMainVM.setControlOptionsState(false)
                btnStart.zoomAnimation(300L, true)
                btnStop.zoomAnimation(300L, false)
            }

        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.Builder().fromUri(
            "mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7")) {
            if (!GeolocationUtils.isPermissionGranted(requireContext())) {
                requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                enableLocationComponent(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

            mapboxMap.locationComponent.apply {

                activateLocationComponent(locationComponentActivationOptions)

                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }
        } else {
            requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
//        if (!GeolocationUtils.isPermissionGranted(requireContext())) {
//            requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        } else {
//            showLocationPermissionRequireDialog()
//        }
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    private val requestLocationPermissionsLaunch =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                val provideRationale =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        false
                    }
                if (provideRationale) {
                    showLocationPermissionRequireDialog()
                }
            }
        }

    private fun showLocationPermissionRequireDialog() {
        val notificationDialog = NotificationDialog.Builder()
            .setTitle(getString(R.string.permission_location_title))
            .setDescription(getString(R.string.permission_location_desc))
            .build()
        notificationDialog.show(childFragmentManager, "PermissionNotificationDialog")
    }


}