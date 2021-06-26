package kz.das.dasaccounting.ui.drivers

import androidx.appcompat.widget.AppCompatImageView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory


fun AppCompatImageView.setTsTypeImage(transportInventory: TransportInventory) {
    when (transportInventory.tsType) {
        TransportType.TRAILED.type -> {
            this.setImageResource(R.drawable.ic_trailer)
        }
        TransportType.TRANSPORT.type -> {
            this.setImageResource(R.drawable.ic_tractor)
        }
        else -> {
            this.setImageResource(R.drawable.ic_tractor)
        }
    }
}