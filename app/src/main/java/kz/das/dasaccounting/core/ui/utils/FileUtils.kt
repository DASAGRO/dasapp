package kz.das.dasaccounting.core.ui.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*

private val fileName = "request_logs.txt"

fun getImageFileMultipart(context: Context, path: Uri): MultipartBody.Part? {
    val file: File? = File(getRealLocalPathFromURI(context, path)?: "")
    return if (file?.exists() == true) {
        val reqFile: RequestBody? = file.asRequestBody("*/*".toMediaTypeOrNull())
        reqFile?.let { MultipartBody.Part.createFormData("image", file.name, it) }
    } else null

}


fun getFileMultipart(context: Context, path: Uri): MultipartBody.Part? {
    val file: File? = File(getRealLocalPathFromURI(context, path)?: "")
    return if (file?.exists() == true) {
        val reqFile: RequestBody? = file.asRequestBody("*/*".toMediaTypeOrNull())
        reqFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }
    } else null

}


fun getRealLocalPathFromURI(mContext: Context, uri: Uri): String? {
    // DocumentProvider
    if (DocumentsContract.isDocumentUri(mContext, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            return getDataColumn(mContext, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                    split[1]
            )
            return contentUri?.let { getDataColumn(mContext, it, selection, selectionArgs) }
        }
    } else return if ("content".equals(uri.scheme, ignoreCase = true)) {

        // Return the remote address
        if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(mContext, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        uri.path
    } else {
        uri.toString()
    }
    return null
}


fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } catch (e: Exception) {
        e.message
    } finally {
        cursor?.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun writeObjectToLog(stringToSave: String, context: Context) {
    val outStream: FileOutputStream?
    var osw: OutputStreamWriter? = null

    try {
        outStream = context.openFileOutput(fileName, Context.MODE_APPEND)
        osw = OutputStreamWriter(outStream)
        osw.write(stringToSave)
        osw.flush()
    } finally {
        osw?.close()
    }
}

 fun readFile(context: Context): String {
    var inStream: FileInputStream? = null
    var myInputStreamReader: InputStreamReader? = null
    var myBufferedReader: BufferedReader? = null
    var retrievedString = ""
    try {
        inStream = context.openFileInput(fileName)
        myInputStreamReader = InputStreamReader(inStream!!)
        myBufferedReader = BufferedReader(myInputStreamReader)
        var readLineString: String? = myBufferedReader.readLine()
        while (readLineString != null) {
            retrievedString += readLineString
            readLineString = myBufferedReader.readLine()
        }
    } finally {
        myBufferedReader!!.close()
        myInputStreamReader!!.close()
        inStream!!.close()
    }
    return retrievedString
}

fun clearLogs(context: Context) {
    var outStream: FileOutputStream? = null

    try {
        outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        outStream.write("".toByteArray())
    } finally {
        outStream!!.close()
    }
}