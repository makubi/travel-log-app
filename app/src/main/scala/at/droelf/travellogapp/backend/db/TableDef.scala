package at.droelf.travellogapp.backend.db

import java.util

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.{Cursor, DatabaseUtils}
import android.util.Log
import at.droelf.travellogapp.{UploadImage, ColumnType}
import scala.collection.JavaConversions._

case class ColumnDef(name: String, columnType: ColumnType)

case class TableDef(name: String, columns: List[ColumnDef]) {

  def selectAll(database: SQLiteDatabase) = {
    val map = new util.ArrayList[RowData]()

    val query: Cursor = DatabaseHelper.query(database, name)

    val queryColumns = columns.map{ column => (column, query.getColumnIndex(column.name)) }

    while(query.moveToNext()) {
      val columnData = queryColumns.map{ case (column, columnIndex) => {
        val data = column.columnType match {
          case ColumnType.TEXT => query.getString(columnIndex)
          case ColumnType.INTEGER => query.getLong(columnIndex)
          case _ => throw new RuntimeException("Unknown ColumnType: " + column.columnType)
        }

        ColumnData(column, data)
      }}

      map.add(RowData(1, columnData))
    }

    query.close()

    map.toList
  }
}

case class ColumnData(columnDef: ColumnDef, data: Any)

case class RowData(number: Int, columnData: List[ColumnData])

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