package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.driver.TransportInventoryEntity

class DriverInventoryTypeConvertor {
    internal var gson = Gson()

    @TypeConverter
    fun stringToTransportInventory(data: String?): TransportInventoryEntity? {
        val listType = object : TypeToken<TransportInventoryEntity>() {}.type
        return gson.fromJson<TransportInventoryEntity>(data, listType)
    }

    @TypeConverter
    fun transportTransportToString(courierEntity: TransportInventoryEntity?): String {
        return gson.toJson(courierEntity)
    }
}