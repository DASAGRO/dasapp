package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.driver.FligelProductEntity

class FligelProductEntityTypeConverter {

    internal var gson = Gson()

    @TypeConverter
    fun stringToFligelProductEntity(data: String?): FligelProductEntity? {
        val listType = object : TypeToken<FligelProductEntity>() {}.type
        return gson.fromJson<FligelProductEntity>(data, listType)
    }

    @TypeConverter
    fun fligelProductEntityToString(courierEntity: FligelProductEntity?): String {
        return gson.toJson(courierEntity)
    }
}