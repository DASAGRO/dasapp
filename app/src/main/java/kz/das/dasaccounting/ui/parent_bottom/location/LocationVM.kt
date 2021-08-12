package kz.das.dasaccounting.ui.parent_bottom.location

import com.google.android.gms.maps.model.LatLng
import kz.das.dasaccounting.core.ui.view_model.BaseVM

class LocationVM: BaseVM() {

    var myLocation: LatLng? = null
    var locationIsSet = false


}