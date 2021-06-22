package kz.das.dasaccounting.data.entities.file

import kz.das.dasaccounting.domain.data.file.File
import java.io.Serializable

data class FileEntity(
    val id: Int? = null,
    val fileName: String? = null,
    val mimeType: String? = null
): Serializable

fun FileEntity.toDomain(): File {
    return File(
        id = this.id,
        fileName = this.fileName,
        mimeType = this.mimeType
    )
}