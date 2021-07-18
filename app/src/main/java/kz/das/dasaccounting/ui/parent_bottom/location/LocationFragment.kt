package kz.das.dasaccounting.ui.parent_bottom.location

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.dialogs.NotificationDialog
import kz.das.dasaccounting.core.ui.extensions.zoomAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentLocationBinding
import kz.das.dasaccounting.domain.common.UserRole
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationVM
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference


/**
 * Use the Mapbox Core Library to receive updates when the device changes location.
 */
class LocationFragment : BaseFragment<LocationVM, FragmentLocationBinding>(), OnMapReadyCallback {

    private var mapboxMap: MapboxMap? = null
    private var map: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var locationEngine: LocationEngine? = null
    private val callback = LocationChangeListeningActivityLocationCallback(this)

    private val coreMainVM: CoreBottomNavigationVM by sharedViewModel()

    override val mViewModel: LocationVM by viewModel()

    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)


    override fun setupUI() {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        mViewBinding.apply {
            btnStart.isVisible = coreMainVM.isOnWork() == false
            btnStop.isVisible = coreMainVM.isOnWork() == true
            coreMainVM.setControlOptionsState(coreMainVM.isOnWork())

            btnStart.setOnClickListener {
                if (coreMainVM.getUserRole() == UserRole.OFFICE.role) {
                    val qrFragment = QrFragment.Builder()
                        .setCancelable(true)
                        .setOnScanCallback(object : QrFragment.OnScanCallback {
                            override fun onScan(qrScan: String) {
                                delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                                    coreMainVM.startWork(qrScan)
                                }
                            }
                        })
                        .build()
                    qrFragment.show(childFragmentManager, "QrShiftFragment")
                } else {
                    coreMainVM.startWork()
                }
            }

            btnStop.setOnClickListener {
                coreMainVM.stopWork()
            }

            map = this.mapView
            map?.getMapAsync(this@LocationFragment)
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        coreMainVM.isWorkStarted().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                coreMainVM.setControlOptionsState(true)
                mViewBinding.btnStart.zoomAnimation(300L, false)
                mViewBinding.btnStop.zoomAnimation(300L, true)
            }
        })

        coreMainVM.isWorkStopped().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                coreMainVM.setControlOptionsState(false)
                mViewBinding.btnStart.zoomAnimation(300L, true)
                mViewBinding.btnStop.zoomAnimation(300L, false)
            }
        })
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { style -> enableLocationComponent(style) }
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component
            val locationComponent = mapboxMap!!.locationComponent

            // Set the LocationComponent activation options
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .build()

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions)

            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS
            initLocationEngine()
        } else {
            showLocationPermissionRequireDialog()
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()

        locationEngine?.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine?.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private class LocationChangeListeningActivityLocationCallback internal constructor(fragment: LocationFragment) :
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<LocationFragment> = WeakReference(fragment)

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        override fun onSuccess(result: LocationEngineResult) {
            val fragment = activityWeakReference.get()
            if (fragment != null) {
                val location = result.lastLocation ?: return

                // Pass the new location to the Maps SDK's LocationComponent
                if (fragment.mapboxMap != null && result.lastLocation != null) {
                    try {
                        fragment.mapboxMap?.locationComponent?.forceLocationUpdate(result.lastLocation)
                    } catch (e: Exception) { }
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        override fun onFailure(exception: Exception) {
            val fragment = activityWeakReference.get()
            if (fragment != null) { }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        map?.onCreate(savedInstanceState)
        map?.onStart()
    }


    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine?.removeLocationUpdates(callback)
        map?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map?.onLowMemory()
    }

    companion object {
        private const val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
        private const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

        fun getScreen() = DasAppScreen(LocationFragment())

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
            } else {
                mapboxMap?.setStyle(
                    Style.MAPBOX_STREETS
                ) { style -> enableLocationComponent(style) }
            }
        }

    private fun showLocationPermissionRequireDialog() {
        val notificationDialog = NotificationDialog.Builder()
            .setTitle(getString(R.string.permission_location_title))
            .setDescription(getString(R.string.permission_location_desc))
            .setOnConfirmCallback(object : NotificationDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            })
            .build()
        notificationDialog.show(childFragmentManager, "PermissionNotificationDialog")
    }


}