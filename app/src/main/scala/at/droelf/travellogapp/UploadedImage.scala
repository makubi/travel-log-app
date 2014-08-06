package at.droelf.travellogapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db._
import org.joda.time.{DateTime, LocalDateTime}

case class UploadedImage(id: Long, uploadedImage: UploadImage, uploadTimeStamp: DateTime)


object UploadedImageTable {
  val tableName = DatabaseOpenHelper.UPLOADED_IMAGE_TABLE

  val idColumn = ColumnDef("_id", ColumnType.Integer)
  val imageId = ColumnDef("imageId", ColumnType.Integer)
  val uploadTimeStamp = ColumnDef("uploadTimeStamp", ColumnType.Text)

  val primaryKeyConstraint = ColumnConstraintDef(List(idColumn), ColumnConstraint.PrimaryKey)

  val columns = idColumn :: imageId :: uploadTimeStamp :: Nil
  val constraints = primaryKeyConstraint :: Nil
  val tableDef = TableDef(tableName, columns, constraints)
}
class UploadedImageTable(database: SQLiteDatabase) {

  val table = new Table(database, UploadedImageTable.tableDef)

  def getAllRows = table.selectAll().map(UploadedImageRow(_))

  def insertRow(imageId: Long, uploadTimeStamp: String) {
    val contentValues = new ContentValues
    contentValues.put(UploadedImageTable.imageId.name, imageId:java.lang.Long)
    contentValues.put(UploadedImageTable.uploadTimeStamp.name, uploadTimeStamp)

    table.insert(contentValues)
  }

  def createTable() = table.createTable()

  def deleteRow(id: Long) = {
    table.delete("_id=?",Array{id.toString})
  }

  def getAllUploadedImages = {
    val uploadImageTable = new UploadImageTable(database)
    val uploadImages = uploadImageTable.getAllRows

    getAllRows.map(row =>
      UploadedImage(
        row.id,
        UploadImage(uploadImages.find(image => row.imageId == image.id).get),
        DateTimeUtils.isoStringToDateTime(row.uploadTimeStamp)
      )
    )
  }
}

case class UploadedImageRow(rowData: RowData) extends ColumnExtractor {

  implicit val implicitRowData = rowData

  def id: Long = getDataForColumn(UploadedImageTable.idColumn)
  def imageId: Long = getDataForColumn(UploadedImageTable.imageId)
  def uploadTimeStamp: String = getDataForColumn(UploadedImageTable.uploadTimeStamp)
}
