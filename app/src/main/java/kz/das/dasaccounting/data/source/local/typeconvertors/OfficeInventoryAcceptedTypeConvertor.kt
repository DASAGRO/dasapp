package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.office.OfficeInventoryAcceptedEntity

class OfficeInventoryAcceptedTypeConvertor {
    internal var gson = Gson()

    @TypeConverter
    fun stringToOfficeAcceptedInventory(data: String?): OfficeInventoryAcceptedEntity? {
        val listType = object : TypeToken<OfficeInventoryAcceptedEntity>() {}.type
        return gson.fromJson<OfficeInventoryAcceptedEntity>(data, listType)
    }

    @TypeConverter
    fun officeAcceptedInventoryToString(courierEntity: OfficeInventoryAcceptedEntity?): String {
        return gson.toJson(courierEntity)
    }
}