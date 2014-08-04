package at.droelf.travellogapp.backend.db

import java.util

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import at.droelf.travellogapp.{ColumnConstraint, ColumnType}

import scala.collection.JavaConversions._

class Table(database: SQLiteDatabase, tableDef: TableDef) {

  val name = tableDef.name
  val columnDefs = tableDef.columnDefs
  val columnConstraintDefs = tableDef.constraintDefs

  def selectAll() = {
    val allRowData = new util.ArrayList[RowData]()

    val query: Cursor = DatabaseHelper.query(database, name)

    val columnsWithIndex = columnDefs.map{ columnDef => (columnDef, query.getColumnIndex(columnDef.name)) }

    while(query.moveToNext()) {
      val columnData = columnsWithIndex.map{ case (column, columnIndex) => {
        val data = column.columnType match {
          case ColumnType.Text => query.getString(columnIndex)
          case ColumnType.Integer => query.getLong(columnIndex)
          case _ => throw new RuntimeException("Unknown ColumnType: " + column.columnType)
        }

        ColumnData(column, data)
      }}

      allRowData.add(RowData(columnData))
    }

    query.close()

    allRowData.toList
  }

  def insert(contentValues: ContentValues) {
    DatabaseHelper.insert(database, name, contentValues)
  }

  def createTable() {
    val columnDefString = columnDefs.map( columnDef => {
      s"${columnDef.name} ${columnDef.columnType.text}"
    })

    val constraintDefString = columnConstraintDefs.map( columnConstraintDef => {
      columnConstraintDef.constraint match {
        case ColumnConstraint.PrimaryKey | ColumnConstraint.Unique => {
          s"${columnConstraintDef.constraint.before} (${columnConstraintDef.columns.map(_.name).mkString(", ")}) ${columnConstraintDef.constraint.after}"
        }
        case _ => throw new RuntimeException("Unknown column constraint: " + columnConstraintDef.constraint)
      }
    })

    val columnString = (columnDefString ::: constraintDefString).mkString(", ")

    DatabaseHelper.createTable(database, name, columnString)
  }
}

case class ColumnDef(name: String, columnType: ColumnType)

case class TableDef(name: String, columnDefs: List[ColumnDef], constraintDefs: List[ColumnConstraintDef] = Nil)

case class ColumnConstraintDef(columns: List[ColumnDef], constraint: ColumnConstraint)

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