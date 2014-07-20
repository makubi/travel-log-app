package at.droelf.travellogapp.backend.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.{Cursor, DatabaseUtils}
import android.util.Log

object DatabaseHelper {
  def update(database: SQLiteDatabase, table: String, values: ContentValues, whereClause: String, whereArgs: Array[String]) {
    val escapedTable: String = getSQLEscapeString(table)
    Log.d("Database", "Updating " + database.getPath + ", " + escapedTable + ", " + whereClause + ", [" + whereArgs.mkString(",") + "], " + values)
    database.update(escapedTable, values, whereClause, whereArgs)
  }

  def queryOrderBy(database: SQLiteDatabase, table: String, orderByColumn: String): Cursor = {
    val escapedTable: String = getSQLEscapeString(table)
    Log.d("Database", "Querying " + database.getPath + ", " + escapedTable + ", " + orderByColumn)
    return database.query(escapedTable, null, null, null, null, null, orderByColumn)
  }

  def insert(database: SQLiteDatabase, table: String, values: ContentValues) {
    val escapedTable: String = getSQLEscapeString(table)
    Log.d("Database", "Inserting " + database.getPath + ", " + escapedTable + ", " + values)
    database.insert(escapedTable, null, values)
  }

  def query(database: SQLiteDatabase, table: String): Cursor = {
    val escapedTable: String = getSQLEscapeString(table)
    Log.d("Database", "Querying " + database.getPath + ", " + escapedTable)
    return database.query(escapedTable, null, null, null, null, null, null)
  }

  private def getSQLEscapeString(string: String): String = {
    return DatabaseUtils.sqlEscapeString(string)
  }

  def createTable(database: SQLiteDatabase, tableName: String, columnString: String) {
    val escapedTable: String = getSQLEscapeString(tableName)
    val createTableStatement: String = "CREATE TABLE " + escapedTable + " (" + columnString + ")"
    Log.d("Database", "Executing SQL statement " + database.getPath + ", " + createTableStatement)
    database.execSQL(createTableStatement)
  }

  def renameTable(database: SQLiteDatabase, oldTableName: String, newTableName: String) {
    val escapedOldTable: String = getSQLEscapeString(oldTableName)
    val escapedNewTable: String = getSQLEscapeString(newTableName)
    val renameTableStatement: String = "ALTER TABLE " + escapedOldTable + " RENAME TO " + escapedNewTable
    Log.d("Database", "Executing SQL statement " + database.getPath + ", " + renameTableStatement)
    database.execSQL(renameTableStatement)
  }

  def delete(database: SQLiteDatabase, table: String, whereClause: String, whereArgs: Array[String]) {
    val escapedTable: String = getSQLEscapeString(table)
    Log.d("Database", "Deleting " + database.getPath + ", " + escapedTable + ", " + whereClause + ", [" + whereArgs.mkString(",") + "]")
    database.delete(escapedTable, whereClause, whereArgs)
  }

  def deleteTable(database: SQLiteDatabase, tableName: String) {
    val escapedTable: String = getSQLEscapeString(tableName)
    val dropTableStatement: String = "DROP TABLE " + escapedTable
    Log.d("Database", "Executing SQL statement " + database.getPath + ", " + dropTableStatement)
    database.execSQL(dropTableStatement)
  }

  def isTableExisting(database: SQLiteDatabase, tableName: String): Boolean = {
    val escapedTable: String = getSQLEscapeString(tableName)
    val tableExistsStatement: String = "select DISTINCT tbl_name from sqlite_master where tbl_name = " + escapedTable
    val cursor: Cursor = database.rawQuery(tableExistsStatement, null)
    Log.d("Database", "Executing SQL statement " + database.getPath + ", " + tableExistsStatement)
    val tableExist: Boolean = cursor.getCount > 0
    cursor.close
    return tableExist
  }
}
