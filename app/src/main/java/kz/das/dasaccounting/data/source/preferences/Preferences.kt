package kz.das.dasaccounting.data.source.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import kz.das.dasaccounting.domain.data.Profile

private const val PREFERENCES_USER_ACCESS_TOKEN = "user_access_token"
private const val PREFERENCES_USER_PROFILE = "user_profile"


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

    fun clearUser() {
        preferences.edit().remove(PREFERENCES_USER_PROFILE).apply()
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