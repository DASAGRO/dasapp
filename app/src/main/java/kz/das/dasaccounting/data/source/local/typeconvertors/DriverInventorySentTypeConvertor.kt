package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.driver.SentTransportEntity

class DriverInventorySentTypeConvertor {

    internal var gson = Gson()

    @TypeConverter
    fun stringToSenTransportInventory(data: String?): SentTransportEntity? {
        val listType = object : TypeToken<SentTransportEntity>() {}.type
        return gson.fromJson<SentTransportEntity>(data, listType)
    }

    @TypeConverter
    fun transportSentTransportToString(courierEntity: SentTransportEntity?): String {
        return gson.toJson(courierEntity)
    }

}