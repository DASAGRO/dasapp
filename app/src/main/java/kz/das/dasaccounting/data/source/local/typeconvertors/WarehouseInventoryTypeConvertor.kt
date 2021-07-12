package kz.das.dasaccounting.data.source.local.typeconvertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.das.dasaccounting.data.entities.warehouse.WarehouseInventoryEntity

class WarehouseInventoryTypeConvertor {
    internal var gson = Gson()

    @TypeConverter
    fun stringToWarehouseInventory(data: String?): WarehouseInventoryEntity? {
        val listType = object : TypeToken<WarehouseInventoryEntity>() {}.type
        return gson.fromJson<WarehouseInventoryEntity>(data, listType)
    }

    @TypeConverter
    fun officeWarehouseToString(entity: WarehouseInventoryEntity?): String {
        return gson.toJson(entity)
    }
}