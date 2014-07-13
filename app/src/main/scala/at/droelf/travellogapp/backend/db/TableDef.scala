package at.droelf.travellogapp.backend.db

import java.util

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.{Cursor, DatabaseUtils}
import android.util.Log
import at.droelf.travellogapp.{UploadImage, ColumnType}
import scala.collection.JavaConversions._

class Table(database: SQLiteDatabase, name: String, columnDefs: List[ColumnDef]) {

  def selectAll() = {
    val allRowData = new util.ArrayList[RowData]()

    val query: Cursor = DatabaseHelper.query(database, name)

    val columnsWithIndex = columnDefs.map{ columnDef => (columnDef, query.getColumnIndex(columnDef.name)) }

    while(query.moveToNext()) {
      val columnData = columnsWithIndex.map{ case (column, columnIndex) => {
        val data = column.columnType match {
          case ColumnType.TEXT => query.getString(columnIndex)
          case ColumnType.INTEGER => query.getLong(columnIndex)
          case _ => throw new RuntimeException("Unknown ColumnType: " + column.columnType)
        }

        ColumnData(column, data)
      }}

      allRowData.add(RowData(columnData))
    }

    query.close()

    allRowData.toList
  }

  def insert(database: SQLiteDatabase, contentValues: ContentValues) {
    DatabaseHelper.insert(database, name, contentValues)
  }
}

case class ColumnDef(name: String, columnType: ColumnType)

case class TableDef(name: String, columnDefs: List[ColumnDef])

case class ColumnData(columnDef: ColumnDef, data: Any)

case class RowData(columnData: List[ColumnData])

trait ColumnExtractor {
  def getDataForColumn[T](column: ColumnDef)(implicit rowData: RowData): T = {
    getColumnData(rowData, column).get.data.asInstanceOf[T]
  }

  private def getColumnData(rowData: RowData, column: ColumnDef) = {
    rowData.columnData.find(e => {
      e.columnDef.name == column.name
    })
  }
}