package kz.das.dasaccounting.data.source.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import kz.das.dasaccounting.data.entities.common.ShiftRequest
import kz.das.dasaccounting.data.entities.driver.FligelProductEntity
import kz.das.dasaccounting.domain.data.Location
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.domain.data.action.InventoryRetain
import kz.das.dasaccounting.domain.data.drivers.FligelProduct

private const val PREFERENCES_USER_ACCESS_TOKEN = "user_access_token"
private const val PREFERENCES_USER_PROFILE = "user_profile"
private const val PREFERENCES_USER_ON_WORK = "user_on_work"
private const val PREFERENCES_HISTORY_INVENTORIES_IS_LOADED = "history_invents_loaded"

private const val PREFERENCES_LAST_LOCATION = "last_location"
private const val PREFERENCES_AWAIT_START_WORK = "await_start_work"
private const val PREFERENCES_AWAIT_FINISH_WORK = "await_finish_work"

private const val PREFERENCES_LAST_FLIGEL_PRODUCT = "last_fligel_product"
private const val PREFERENCES_LAST_FLIGEL_PRODUCT_CNT = "last_fligel_product_cnt"

private const val PREFERENCES_OPENED_INVENTORY = "opened_inventory"
private const val PREFERENCES_START_QR_SCAN = "start_qr_scan"

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

    fun setAwaitStartWork(shiftRequest: ShiftRequest) {
        preferences.edit().putString(PREFERENCES_AWAIT_START_WORK, Gson().toJson(shiftRequest)).apply()
    }

    fun setAwaitFinishWork(shiftRequest: ShiftRequest) {
        preferences.edit().putString(PREFERENCES_AWAIT_FINISH_WORK, Gson().toJson(shiftRequest)).apply()
    }

    fun clearAwaitStartWork() {
        preferences.edit().remove(PREFERENCES_AWAIT_START_WORK).apply()
    }

    fun clearAwaitFinishWork() {
        preferences.edit().remove(PREFERENCES_AWAIT_FINISH_WORK).apply()
    }

    fun getAwaitStartWork(): ShiftRequest? {
        return try {
            val json = preferences.getString(PREFERENCES_AWAIT_START_WORK, null)
            Gson().fromJson(json, ShiftRequest::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getAwaitFinishWork(): ShiftRequest? {
        return try {
            val json = preferences.getString(PREFERENCES_AWAIT_FINISH_WORK, null)
            Gson().fromJson(json, ShiftRequest::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clearUser() {
        preferences.edit().remove(PREFERENCES_LAST_FLIGEL_PRODUCT).apply()
        preferences.edit().remove(PREFERENCES_LAST_FLIGEL_PRODUCT_CNT).apply()
        preferences.edit().remove(PREFERENCES_USER_ACCESS_TOKEN).apply()
        preferences.edit().remove(PREFERENCES_USER_PROFILE).apply()
        preferences.edit().remove(PREFERENCES_USER_ON_WORK).apply()
        preferences.edit().remove(PREFERENCES_HISTORY_INVENTORIES_IS_LOADED).apply()
        preferences.edit().remove(PREFERENCES_LAST_LOCATION).apply()
        preferences.edit().remove(PREFERENCES_AWAIT_START_WORK).apply()
        preferences.edit().remove(PREFERENCES_AWAIT_FINISH_WORK).apply()
    }

    fun setHistoryInventoriesLoad(value: Boolean) {
        preferences.edit().putBoolean(PREFERENCES_HISTORY_INVENTORIES_IS_LOADED, value).apply()
    }

    fun isHistoryInventoriesLoaded(): Boolean {
        return preferences.getBoolean(PREFERENCES_HISTORY_INVENTORIES_IS_LOADED, false)
    }

    fun isUserOnWork(): Boolean {
        return preferences.getBoolean(PREFERENCES_USER_ON_WORK, true)
    }

    fun startWork() {
        preferences.edit().putBoolean(PREFERENCES_USER_ON_WORK, true).apply()
    }

    fun finishWork() {
        preferences.edit().putBoolean(PREFERENCES_USER_ON_WORK, false).apply()
    }

    fun saveLastFligelProduct(fligelProduct: FligelProduct) {
        preferences.edit().putString(PREFERENCES_LAST_FLIGEL_PRODUCT, Gson().toJson(fligelProduct)).apply()
    }

    fun getLastFligelProduct(): FligelProduct? {
        return try {
            val json = preferences.getString(PREFERENCES_LAST_FLIGEL_PRODUCT, null)
            Gson().fromJson(json, FligelProduct::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun saveLastFligelProductCnt(count: Int) {
        preferences.edit().putInt(PREFERENCES_LAST_FLIGEL_PRODUCT_CNT, count).apply()
    }

    fun getLastFligelProductCnt(): Int {
        return preferences.getInt(PREFERENCES_LAST_FLIGEL_PRODUCT_CNT, 0)
    }

    fun saveLastLocation(location: Location) {
        preferences.edit().putString(PREFERENCES_LAST_LOCATION, Gson().toJson(location)).apply()
    }

    fun getLastLocation(): Location {
        return try {
            val json = preferences.getString(PREFERENCES_LAST_LOCATION, null)
            Gson().fromJson(json, Location::class.java)
        } catch (e: Exception) {
            Location(0.0, 0.0)
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

    fun saveInventory(inventoryRetain: InventoryRetain) {
        preferences.edit().putString(PREFERENCES_OPENED_INVENTORY, Gson().toJson(inventoryRetain)).apply()
    }

    fun getSavedInventory(): InventoryRetain? {
        return try {
            val json = preferences.getString(PREFERENCES_OPENED_INVENTORY, null)
            Gson().fromJson(json, InventoryRetain::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteSavedInventory() {
        preferences.edit().remove(PREFERENCES_OPENED_INVENTORY).apply()
    }

    fun saveStartQrScan(qrScan: String) {
        preferences.edit().putString(PREFERENCES_START_QR_SCAN, qrScan).apply()
    }

    fun getStartQrScan(): String? {
        return preferences.getString(PREFERENCES_START_QR_SCAN, null)
    }

    fun deleteStartQrScan(){
        preferences.edit().remove(PREFERENCES_START_QR_SCAN).apply()
    }
}