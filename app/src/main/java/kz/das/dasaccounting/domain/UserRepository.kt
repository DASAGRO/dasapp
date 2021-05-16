package kz.das.dasaccounting.domain

interface UserRepository {

    fun saveUser()

    fun getUser()

    fun changeAvatar()

    fun deleteAvatar()

    fun saveUserCoordinates(lat: Double, long: Double)

    fun getUserCoordinates(): String?

}