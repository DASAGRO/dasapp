package kz.das.dasaccounting.data.entities.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class TransferItem (
    var storeUUIDSender: String? = null,
    var storeUUIDReceiver: String? = null,
    var senderUUID: String? = null,
    var receiverUUID: String? = null,
    var senderName: String? = null,
    var receiverName: String? = null,
    var transferType: String? = null
): Serializable

class TransferItemTypeConvertor {

    internal var gson = Gson()

    @TypeConverter
    fun stringToTransferItemInventory(data: String?): TransferItem? {
        val listType = object : TypeToken<TransferItem>() {}.type
        return gson.fromJson<TransferItem>(data, listType)
    }

    @TypeConverter
    fun transferItemToString(item: TransferItem?): String {
        return gson.toJson(item)
    }

}