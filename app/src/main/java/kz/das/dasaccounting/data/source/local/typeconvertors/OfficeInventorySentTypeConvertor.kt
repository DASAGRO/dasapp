package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.office.OfficeInventorySentEntity

class OfficeInventorySentTypeConvertor {
    internal var gson = Gson()

    @TypeConverter
    fun stringToOfficeSentInventory(data: String?): OfficeInventorySentEntity? {
        val listType = object : TypeToken<OfficeInventorySentEntity>() {}.type
        return gson.fromJson<OfficeInventorySentEntity>(data, listType)
    }

    @TypeConverter
    fun officeSentInventoryToString(courierEntity: OfficeInventorySentEntity?): String {
        return gson.toJson(courierEntity)
    }
}