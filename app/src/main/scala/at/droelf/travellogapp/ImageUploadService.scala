package at.droelf.travellogapp

import java.util

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.joda.time.LocalDateTime

object ImageUploadService {
  def getInstance: ImageUploadService = {
    return INSTANCE
  }

  private final val INSTANCE: ImageUploadService = new ImageUploadService
}

class ImageUploadService {

  private[travellogapp] def queueImageUpload(name: String, imageTakenDateTime: LocalDateTime, timezone: String, localImagePath: String) {
    val database: SQLiteDatabase = databaseOpenHelper.getWritableDatabase
    database.beginTransaction
    val contentValues: ContentValues = new ContentValues
    contentValues.put("name", name)
    contentValues.put("dateTime", DateTimeUtils.localDateTimeToIsoString(imageTakenDateTime))
    contentValues.put("localImagePath", localImagePath)
    DatabaseHelper.insert(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, contentValues)
    database.setTransactionSuccessful
    database.endTransaction
  }

  private[travellogapp] def getQueuedImages: List[UploadImage] = {
    var uploadImageList: List[UploadImage] = Nil
    val database: SQLiteDatabase = databaseOpenHelper.getReadableDatabase
    database.beginTransaction
    val query: Cursor = DatabaseHelper.query(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE)
    val nameIndex: Int = query.getColumnIndex("name")
    val dateTimeIndex: Int = query.getColumnIndex("dateTime")
    val localImagePathIndex: Int = query.getColumnIndex("localImagePath")
    while (query.moveToNext) {
      val name: String = query.getString(nameIndex)
      val dateTime: LocalDateTime = DateTimeUtils.iosStringToLocalDateTime(query.getString(dateTimeIndex))
      val localImagePath: String = query.getString(localImagePathIndex)
//      uploadImageList.add(new UploadImage(dateTime, null, name, localImagePath))

      uploadImageList = UploadImage(dateTime, null, name, localImagePath) :: uploadImageList
    }
    query.close
    database.setTransactionSuccessful
    database.endTransaction
    return uploadImageList
  }

  private[travellogapp] def setImageUploaded(uploadImage: UploadImage) {
    val database: SQLiteDatabase = databaseOpenHelper.getWritableDatabase
    database.beginTransaction
    DatabaseHelper.delete(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, "dateTime=?", Array[String](DateTimeUtils.localDateTimeToIsoString(uploadImage.dateTime)))
    database.setTransactionSuccessful
    database.endTransaction
  }

  private final val databaseOpenHelper: DatabaseOpenHelper = DatabaseOpenHelper.getInstance
}