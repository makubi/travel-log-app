package at.droelf.travellogapp

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.util.Log;

case class ColumnConstraint(before: String, after: String = "")

object ColumnConstraint {
  val PrimaryKey = ColumnConstraint("PRIMARY KEY")
  val Unique = ColumnConstraint("UNIQUE", "ON CONFLICT ROLLBACK")
}


case class ColumnType(text: String)

object ColumnType {
  val Text = ColumnType("TEXT")
  val Integer = ColumnType("INTEGER")
}

