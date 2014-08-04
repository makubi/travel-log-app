package at.droelf.travellogapp.backend

import at.droelf.travellogapp.backend.db.{Transaction, TransactionReadOnly, DatabaseHelper, DatabaseOpenHelper}
import at.droelf.travellogapp.{UploadedImageTable, DateTimeUtils, UploadImage, UploadImageTable}
import org.joda.time.{DateTime, LocalDateTime}

object UploadedImageService {

  def getUploadedImages = TransactionReadOnly { db =>
    val table = new UploadedImageTable(db)
    table.getAllUploadedImages
  }

}