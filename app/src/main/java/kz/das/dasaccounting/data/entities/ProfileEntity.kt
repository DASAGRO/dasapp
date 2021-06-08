package kz.das.dasaccounting.data.entities

import kz.das.dasaccounting.domain.data.Profile

data class ProfileEntity(
        val activity: Boolean?,
        val firstName: String?,
        val hasPassword: Boolean?,
        val id: Int,
        val iin: String?,
        var imagePath: String?,
        val isEightHoured: Boolean?,
        val lastName: String?,
        val middleName: String?,
        val phone: String?,
        val position: String?,
        val token: String?,
        val userId: String?
)

fun ProfileEntity.toDomain(): Profile {
    return Profile(
            activity = this.activity,
            firstName = this.firstName,
            hasPassword = this.hasPassword,
            id = this.id,
            iin = this.iin,
            imagePath = this.imagePath,
            isEightHoured = this.isEightHoured,
            lastName = this.lastName,
            middleName = this.middleName,
            phone = this.phone,
            position = this.position,
            token = this.token,
            userId = this.userId
    )
}