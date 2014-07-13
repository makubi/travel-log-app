package at.droelf.travellogapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db._
import org.joda.time.LocalDateTime

case class UploadImage(id: Long, dateTime: LocalDateTime, timezone: String, name: String, imagePath: String)

object UploadImageTable {
  val tableName = DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE

  val idColumn = ColumnDef("_id", ColumnType.INTEGER)
  val nameColumn = ColumnDef("name", ColumnType.TEXT)
  val dateTimeColumn = ColumnDef("dateTime", ColumnType.TEXT)
  val localImagePathColumn = ColumnDef("localImagePath", ColumnType.TEXT)
  val timeZoneColumn = ColumnDef("timeZone", ColumnType.TEXT)

  val columns = idColumn :: nameColumn :: dateTimeColumn :: localImagePathColumn :: timeZoneColumn :: Nil
  val tableDef = TableDef(tableName, columns)
}
class UploadImageTable(database: SQLiteDatabase) {

  val table = new Table(database, UploadImageTable.tableName, UploadImageTable.columns)

  def getAllRows = table.selectAll().map(UploadImageRow(_))

  def insertRow(name: String, dateTime: String, localImagePath: String, timeZone: String) {
    val contentValues = new ContentValues
    contentValues.put(UploadImageTable.nameColumn.name, name)
    contentValues.put(UploadImageTable.dateTimeColumn.name, dateTime)
    contentValues.put(UploadImageTable.localImagePathColumn.name, localImagePath)
    contentValues.put(UploadImageTable.timeZoneColumn.name, timeZone)

    table.insert(database, contentValues)
  }
}

case class UploadImageRow(rowData: RowData) extends ColumnExtractor {

  implicit val implicitRowData = rowData

  def id: Long = getDataForColumn(UploadImageTable.idColumn)
  def name: String = getDataForColumn(UploadImageTable.nameColumn)
  def dateTime: String = getDataForColumn(UploadImageTable.dateTimeColumn)
  def localImagePath: String = getDataForColumn(UploadImageTable.localImagePathColumn)
  def timeZone: String = getDataForColumn(UploadImageTable.timeZoneColumn)
}
