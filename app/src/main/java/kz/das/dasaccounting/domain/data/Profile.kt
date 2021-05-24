package kz.das.dasaccounting.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Profile(
    val activity: Boolean?,
    val firstName: String?,
    val hasPassword: Boolean?,
    val id: Int,
    val iin: String?,
    val isEightHoured: Boolean?,
    val lastName: String?,
    val middleName: String?,
    val phone: String?,
    val position: String?,
    var token: String?,
    val userId: String?
): Parcelable
