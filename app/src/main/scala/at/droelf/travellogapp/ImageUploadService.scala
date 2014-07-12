package at.droelf.travellogapp

import java.util

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.joda.time.LocalDateTime
import scala.collection.JavaConversions._

object ImageUploadService {
  def getInstance: ImageUploadService = {
    return INSTANCE
  }

  private final val INSTANCE: ImageUploadService = new ImageUploadService
}

class ImageUploadService {

  val databaseOpenHelper: DatabaseOpenHelper = DatabaseOpenHelper.getInstance

  def queueImageUpload(name: String, imageTakenDateTime: LocalDateTime, timezone: String, localImagePath: String) {
    val database: SQLiteDatabase = databaseOpenHelper.getWritableDatabase

    execInTransaction(database, {

      val contentValues = new ContentValues
      contentValues.put("name", name)
      contentValues.put("dateTime", DateTimeUtils.localDateTimeToIsoString(imageTakenDateTime))
      contentValues.put("localImagePath", localImagePath)
      contentValues.put("timeZone", timezone)

      DatabaseHelper.insert(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, contentValues)

    })
  }

  def getQueuedImages: List[UploadImage] = {
    val database: SQLiteDatabase = databaseOpenHelper.getReadableDatabase

    execInTransaction[java.util.List[UploadImage]](database, {

      val uploadImageList: java.util.List[UploadImage] = new util.ArrayList[UploadImage]()

      val query: Cursor = DatabaseHelper.query(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE)

      val idIndex: Int = query.getColumnIndex("_id")
      val nameIndex: Int = query.getColumnIndex("name")
      val dateTimeIndex: Int = query.getColumnIndex("dateTime")
      val localImagePathIndex: Int = query.getColumnIndex("localImagePath")
      val timezoneIndex = query.getColumnIndex("timeZone")

      while (query.moveToNext) {
        val id = query.getLong(idIndex)
        val name = query.getString(nameIndex)
        val dateTime = DateTimeUtils.iosStringToLocalDateTime(query.getString(dateTimeIndex))
        val localImagePath = query.getString(localImagePathIndex)
        val timeZone = query.getString(timezoneIndex)

        uploadImageList.add(UploadImage(id, dateTime, timeZone, name, localImagePath))
      }

      query.close

      uploadImageList

    }).toList
  }

  def setImageUploaded(id: Long) {
    val database: SQLiteDatabase = databaseOpenHelper.getWritableDatabase

    execInTransaction[Unit](database, {
      DatabaseHelper.delete(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, "_id=?", Array[String](id.toString))
    })
  }

  def execInTransaction[E](database: SQLiteDatabase, f: => E): E = {
    database.beginTransaction()

    val ret = f

    database.setTransactionSuccessful()
    database.endTransaction()

    ret
  }

}