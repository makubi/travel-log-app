package at.droelf.travellogapp.backend

import _root_.android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db.{Transaction, TransactionReadOnly, DatabaseHelper, DatabaseOpenHelper}
import at.droelf.travellogapp.{UploadedImageTable, DateTimeUtils, UploadImage, UploadImageTable}
import org.joda.time.{DateTime, LocalDateTime}

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

    table.getAllRows.map(UploadImage(_))

  }

  private[backend] def setImageUploaded(id: Long): Unit = Transaction { db =>
    val table = new UploadedImageTable(db)
    table.insertRow(id, DateTimeUtils.dateTimeToIsoString(DateTime.now()))
  }


}