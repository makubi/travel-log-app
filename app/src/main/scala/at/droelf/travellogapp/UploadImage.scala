package at.droelf.travellogapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.backend.db._
import org.joda.time.LocalDateTime

case class UploadImage(id: Long, dateTime: LocalDateTime, timezone: String, name: String, imagePath: String)

object UploadImage{
  def apply(row: UploadImageRow): UploadImage = UploadImage(row.id, DateTimeUtils.isoStringToLocalDateTime(row.dateTime), row.timeZone, row.name, row.localImagePath)
}


object UploadImageTable {
  val tableName = DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE

  val idColumn = ColumnDef("_id", ColumnType.Integer)
  val nameColumn = ColumnDef("name", ColumnType.Text)
  val dateTimeColumn = ColumnDef("dateTime", ColumnType.Text)
  val localImagePathColumn = ColumnDef("localImagePath", ColumnType.Text)
  val timeZoneColumn = ColumnDef("timeZone", ColumnType.Text)

  val primaryKeyConstraint = ColumnConstraintDef(List(idColumn), ColumnConstraint.PrimaryKey)
  val localImagePathConstraint = ColumnConstraintDef(List(localImagePathColumn), ColumnConstraint.Unique)

  val columns = idColumn :: nameColumn :: dateTimeColumn :: localImagePathColumn :: timeZoneColumn :: Nil
  val constraints = primaryKeyConstraint :: localImagePathConstraint :: Nil
  val tableDef = TableDef(tableName, columns, constraints)
}
class UploadImageTable(database: SQLiteDatabase) {

  val table = new Table(database, UploadImageTable.tableDef)

  def getAllRows = table.selectAll().map(UploadImageRow(_))

  def insertRow(name: String, dateTime: String, localImagePath: String, timeZone: String) {
    val contentValues = new ContentValues
    contentValues.put(UploadImageTable.nameColumn.name, name)
    contentValues.put(UploadImageTable.dateTimeColumn.name, dateTime)
    contentValues.put(UploadImageTable.localImagePathColumn.name, localImagePath)
    contentValues.put(UploadImageTable.timeZoneColumn.name, timeZone)

    table.insert(contentValues)
  }

  def createTable() = table.createTable()
}

case class UploadImageRow(rowData: RowData) extends ColumnExtractor {

  implicit val implicitRowData = rowData

  def id: Long = getDataForColumn(UploadImageTable.idColumn)
  def name: String = getDataForColumn(UploadImageTable.nameColumn)
  def dateTime: String = getDataForColumn(UploadImageTable.dateTimeColumn)
  def localImagePath: String = getDataForColumn(UploadImageTable.localImagePathColumn)
  def timeZone: String = getDataForColumn(UploadImageTable.timeZoneColumn)
}
