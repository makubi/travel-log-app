package at.droelf.travellogapp.backend

import java.util

import _root_.android.content.ContentValues
import _root_.android.database.Cursor
import _root_.android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db.{DatabaseOpenHelper, DatabaseHelper}
import at.droelf.travellogapp.{UploadImageTable, DateTimeUtils, UploadImage}
import org.joda.time.LocalDateTime

import scala.collection.JavaConversions._

object ImageUploadService {
  def getInstance: ImageUploadService = {
    return INSTANCE
  }

  private final val INSTANCE: ImageUploadService = new ImageUploadService
}

class ImageUploadService {

  def queueImageUpload(name: String, imageTakenDateTime: LocalDateTime, timezone: String, localImagePath: String) {
    val database: SQLiteDatabase = DatabaseOpenHelper.getWritableDatabase

    val table = new UploadImageTable(database)

    execInTransaction(database, {

      table.insertRow(name, DateTimeUtils.localDateTimeToIsoString(imageTakenDateTime), localImagePath, timezone)
    })
  }

  def getQueuedImages: List[UploadImage] = {
    val database: SQLiteDatabase = DatabaseOpenHelper.getReadableDatabase

    val table = new UploadImageTable(database)

    execInTransaction[List[UploadImage]](database, {

      table.getAllRows.map( row => {
        UploadImage(row.id, DateTimeUtils.iosStringToLocalDateTime(row.dateTime), row.timeZone, row.name, row.localImagePath)
      })
    })
  }

  private[backend] def setImageUploaded(id: Long) {
    val database: SQLiteDatabase = DatabaseOpenHelper.getWritableDatabase

    execInTransaction[Unit](database, {
      // TODO own table with timestamp
      DatabaseHelper.delete(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, "_id=?", Array[String](id.toString))
    })
  }

  private def execInTransaction[E](database: SQLiteDatabase, f: => E): E = {
    database.beginTransaction()

    try {
      val ret = f

      database.setTransactionSuccessful()
      ret
    }
    catch {
      case e: Exception => throw e
    }
    finally {
      database.endTransaction()
    }
  }

}