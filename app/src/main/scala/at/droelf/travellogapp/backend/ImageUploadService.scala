package at.droelf.travellogapp.backend

import _root_.android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db.{Transaction, TransactionReadOnly, DatabaseHelper, DatabaseOpenHelper}
import at.droelf.travellogapp.{DateTimeUtils, UploadImage, UploadImageTable}
import org.joda.time.LocalDateTime

object ImageUploadService {
  def getInstance: ImageUploadService = {
    return INSTANCE
  }

  private final val INSTANCE: ImageUploadService = new ImageUploadService
}

class ImageUploadService {

  def queueImageUpload(name: String, imageTakenDateTime: LocalDateTime, timezone: String, localImagePath: String): Unit = Transaction { db =>
    val table = new UploadImageTable(db)
    table.insertRow(name, DateTimeUtils.localDateTimeToIsoString(imageTakenDateTime), localImagePath, timezone)
  }

  def getQueuedImages: List[UploadImage] = TransactionReadOnly { db =>

    val table = new UploadImageTable(db)

    table.getAllRows.map( row => {
      UploadImage(row.id, DateTimeUtils.iosStringToLocalDateTime(row.dateTime), row.timeZone, row.name, row.localImagePath)
    })

  }

  private[backend] def setImageUploaded(id: Long): Unit = Transaction { db =>
    // TODO own table with timestamp
    DatabaseHelper.delete(db, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, "_id=?", Array[String](id.toString))
  }


}