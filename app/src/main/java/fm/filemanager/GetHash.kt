package fm.filemanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import java.io.File
import java.security.MessageDigest

const val DATABASE_NAME = "file_hashes.db"
const val TABLE_NAME = "file_hashes"
const val COLUMN_NAME = "file_name"
const val COLUMN_HASH = "file_hash"
const val DATABASE_VERSION = 1

class FileHashesDbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NAME ($COLUMN_NAME TEXT PRIMARY KEY, $COLUMN_HASH TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
class HashCheckout {
    private fun md5HashFun(file: File): String {
        val md = MessageDigest.getInstance("MD5")
        return file.inputStream().use { fis ->
            val buffer = ByteArray(8192)
            generateSequence {
                when (val bytesRead = fis.read(buffer)) {
                    -1 -> null
                    else -> bytesRead
                }
            }.forEach {bytesRead -> md.update(buffer, 0, bytesRead)}

            md.digest().joinToString("") { "%02x".format(it) }
        }
    }

    fun compareHashWithBD(context: Context): MutableList<File> {
        val dbHelper = FileHashesDbHelper(context)
        val db = dbHelper.readableDatabase
        val changedFilesList = mutableListOf<File>()
        val path = Environment.getExternalStorageDirectory().path
        val root = File(path)
        val listOfFiles = root.listFiles()
        for (file in listOfFiles) {
            if (!file.isDirectory) {
                val fileName = file.name
                val fileHash = md5HashFun(file)
                val cursor = db.query(
                    TABLE_NAME, arrayOf(COLUMN_HASH),
                    "$COLUMN_NAME=?", arrayOf(fileName),
                    null, null, null
                )
                if (cursor.moveToFirst()) {
                    val savedHash = cursor.getString(cursor.getColumnIndex(COLUMN_HASH))
                    if (fileHash != savedHash) {
                        changedFilesList.add(file)// Хеш-код файла изменился, добавили в список
                    }
                } else {
                    changedFilesList.add(file)// Файл не найден в БД
                }
                cursor.close()
            }
            else {
                //changedFilesList.add(file)// Файл - директория, добавляем ее в список для навигации
            }
        }
        db.close()
        return changedFilesList
    }
    fun saveHashToBD(context: Context) {
            val path = Environment.getExternalStorageDirectory().path
            val root = File(path)
            val listOfFiles = root.listFiles()
            val dbHelper = FileHashesDbHelper(context)
            val db = dbHelper.writableDatabase
            for (file in listOfFiles) {
                if (!file.isDirectory) {
                    val fileName = file.name
                    val fileHash = md5HashFun(file)
                    val values = ContentValues().apply {
                        put(COLUMN_NAME, fileName)
                        put(COLUMN_HASH, fileHash)
                    }
                    db.insert(TABLE_NAME, null, values)
                }
            }
            db.close()
    }
}