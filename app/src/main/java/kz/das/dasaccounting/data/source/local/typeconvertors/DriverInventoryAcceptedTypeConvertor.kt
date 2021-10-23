package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.driver.AcceptedTransportEntity

class DriverInventoryAcceptedTypeConvertor {

    internal var gson = Gson()

    @TypeConverter
    fun stringToAcceptedTransportInventory(data: String?): AcceptedTransportEntity? {
        val listType = object : TypeToken<AcceptedTransportEntity>() {}.type
        return gson.fromJson<AcceptedTransportEntity>(data, listType)
    }

    @TypeConverter
    fun transportAcceptedTransportToString(courierEntity: AcceptedTransportEntity?): String {
        return gson.toJson(courierEntity)
    }

}