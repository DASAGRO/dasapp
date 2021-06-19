package kz.das.dasaccounting.data.source.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile

private const val PREFERENCES_USER_ACCESS_TOKEN = "user_access_token"
private const val PREFERENCES_USER_PROFILE = "user_profile"
private const val PREFERENCES_USER_ON_WORK = "user_on_work"

private const val PREFERENCES_LAST_LOCATION = "last_location"

class UserPreferences(private val preferences: SharedPreferences) {

    fun setUserAccessToken(accessToken: String) {
        Hawk.put(PREFERENCES_USER_ACCESS_TOKEN, accessToken)
    }

    fun getUserAccessToken(): String? {
        return Hawk.get(PREFERENCES_USER_ACCESS_TOKEN)
    }

    fun clearUserAccessToken() {
        Hawk.delete(PREFERENCES_USER_ACCESS_TOKEN)
    }

    fun setUser(user: Profile) {
        preferences.edit().putString(PREFERENCES_USER_PROFILE, Gson().toJson(user)).apply()
    }

    fun setImagePath(imagePath: String) {
        val user = getUser()
        user?.imagePath = imagePath
        user?.let {
            setUser(it)
        }
    }

    fun clearUser() {
        preferences.edit().remove(PREFERENCES_USER_PROFILE).apply()
    }

    fun isUserOnWork(): Boolean {
        return preferences.getBoolean(PREFERENCES_USER_ON_WORK, false)
    }

    fun startWork() {
        preferences.edit().putBoolean(PREFERENCES_USER_ON_WORK, true).apply()
    }

    fun finishWork() {
        preferences.edit().putBoolean(PREFERENCES_USER_ON_WORK, false).apply()
    }

    fun saveLastLocation(location: Location) {
        preferences.edit().putString(PREFERENCES_LAST_LOCATION, Gson().toJson(location)).apply()
    }

    fun getLastLocation(): Location {
        return try {
            val json = preferences.getString(PREFERENCES_LAST_LOCATION, null)
            Gson().fromJson(json, Location::class.java)
        } catch (e: Exception) {
            Location(48.005284, 66.9045434)
        }
    }

    fun getUser(): Profile? {
        return try {
            val json = preferences.getString(PREFERENCES_USER_PROFILE, null)
            Gson().fromJson(json, Profile::class.java)
        } catch (e: Exception) {
            null
        }
    }


}