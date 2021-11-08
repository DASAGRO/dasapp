package kz.das.dasaccounting.ui.parent_bottom.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kz.das.dasaccounting.core.extensions.delayedTask
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.dialogs.NotificationDialog
import kz.das.dasaccounting.core.ui.extensions.zoomAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentLocationBinding
import kz.das.dasaccounting.domain.common.UserRole
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationVM
import kz.das.dasaccounting.ui.parent_bottom.qr.QrFragment
import kz.das.dasaccounting.ui.utils.GeolocationUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kz.das.dasaccounting.R

class LocationFragment : BaseFragment<LocationVM, FragmentLocationBinding>(), OnMapReadyCallback {

    private val coreMainVM: CoreBottomNavigationVM by sharedViewModel()

    private lateinit var mapView: MapView
    private var map: GoogleMap? = null

    override val mViewModel: LocationVM by viewModel()

    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)

    private val requestLocationPermissionsLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            showLocationPermissionRequireDialog()
        } else {
            setupLocation()
        }
    }

    private var scale: Float = 12f
    private var mPositionMarker: Marker? = null

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.apply {
            mapView = mapLocation
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            mapView.getMapAsync(this@LocationFragment)

            checkLocation()

            ivLocation.setOnClickListener {
                checkLocation()
            }

            ivZoomIn.setOnClickListener {
                map?.animateCamera(CameraUpdateFactory.zoomIn())
            }

            ivZoomOut.setOnClickListener {
                map?.animateCamera(CameraUpdateFactory.zoomOut())
            }

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
                if (coreMainVM.getUserRole() == UserRole.OFFICE.role) {
                    val qrFragment = QrFragment.Builder()
                            .setCancelable(true)
                            .setOnScanCallback(object : QrFragment.OnScanCallback {
                                override fun onScan(qrScan: String) {
                                    delayedTask(300L, CoroutineScope(Dispatchers.Main)) {
                                        if (coreMainVM.isQrSessionEqual(qrScan)) {
                                            coreMainVM.stopWork()
                                        } else {
                                            showError(getString(R.string.common_error), getString(R.string.qr_session_error))
                                        }
                                    }
                                }
                            })
                            .build()
                    qrFragment.show(childFragmentManager, "QrShiftFragment")
                } else {
                    coreMainVM.stopWork()
                }
            }

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

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            try {
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
            } catch (e: Resources.NotFoundException) {
                Log.d("Map style file:", "Can't find style. Error: ", e)
            }
            map = it
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocation() {
        if (GeolocationUtils.isPermissionGranted(requireContext()) && map != null) {
            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, true)

            mViewModel.myLocation?.let {
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), scale))
            }

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    mViewModel.myLocation = LatLng(location.latitude, location.longitude)
                    mViewModel.myLocation?.let {
                        drawMarker(it)
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it, scale)
                        map?.animateCamera(cameraUpdate)
                    }
                }

                override fun onStatusChanged(s: String, i: Int, bundle: Bundle) { }

                override fun onProviderEnabled(s: String) { }

                override fun onProviderDisabled(s: String) { }
            }

            provider?.let {
                locationManager.requestLocationUpdates(it, DEFAULT_MAX_WAIT_TIME, 0f, locationListener)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            }

            mViewModel.locationIsSet = true
        }
    }

    private fun drawMarker(location: LatLng) {
        try {
            requireContext().let {
                mViewModel.myLocation = location

                mPositionMarker?.remove()

                val markerOptions = MarkerOptions()
                    .position(location)
                    .title(this.getString(R.string.current_location))
                    .icon(bitmapDescriptorFromVector())
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))

                mPositionMarker = map?.addMarker(markerOptions)
                mPositionMarker?.tag = "user_current_location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun  bitmapDescriptorFromVector(): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun checkLocation() {
        if (GeolocationUtils.isPermissionGranted(requireContext())) {
            setupLocation()
        } else {
            requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLocationPermissionRequireDialog() {
        val notificationDialog = NotificationDialog.Builder()
            .setTitle(getString(R.string.permission_location_title))
            .setDescription(getString(R.string.permission_location_desc))
            .setOnConfirmCallback(object : NotificationDialog.OnConfirmCallback {
                override fun onConfirmClicked() {
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    //requestLocationPermissionsLaunch.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            })
            .build()
        notificationDialog.show(childFragmentManager, "PermissionNotificationDialog")
    }



    companion object {
        private const val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
        private const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

        fun getScreen() = DasAppScreen(LocationFragment())

    }


}