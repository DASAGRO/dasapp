package kz.das.dasaccounting.domain.data.file

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class File(
    val id: Int? = null,
    val fileName: String? = null,
    val mimeType: String? = null
): Parcelable