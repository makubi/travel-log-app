package at.droelf.travellogapp

import android.app.Application
import android.util.Log;

case class ColumnConstraint(text: String)

object ColumnConstraint {
  val PrimaryKey = ColumnConstraint("PRIMARY KEY")
}


case class ColumnType(text: String)

object ColumnType {
  val Text = ColumnType("TEXT")
  val Integer = ColumnType("INTEGER")
}