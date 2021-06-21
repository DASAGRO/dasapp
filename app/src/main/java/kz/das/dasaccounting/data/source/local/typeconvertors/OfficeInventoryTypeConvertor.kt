package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity

class OfficeInventoryEntityTypeConvertor {
    internal var gson = Gson()

    @TypeConverter
    fun stringToOfficeInventory(data: String?): OfficeInventoryEntity? {
        val listType = object : TypeToken<OfficeInventoryEntity>() {}.type
        return gson.fromJson<OfficeInventoryEntity>(data, listType)
    }

    @TypeConverter
    fun officeInventoryToString(courierEntity: OfficeInventoryEntity?): String {
        return gson.toJson(courierEntity)
    }
}