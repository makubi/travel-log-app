package at.droelf.travellogapp

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

object DatabaseOpenHelper {
  def getInstance: DatabaseOpenHelper = {
    return INSTANCE
  }

  private final val INSTANCE: DatabaseOpenHelper = new DatabaseOpenHelper
  final val INITIAL_DB_VERSION: Int = 1
  private final val CURRENT_DATABASE_VERSION = "2"
  private[travellogapp] final val DATABASE_NAME: String = "travelLogDb"
  final val QUEUED_IMAGE_UPLOADS_TABLE: String = "QUEUED_IMAGE_UPLOADS"
}

class DatabaseOpenHelper extends SQLiteOpenHelper(AppStatics.context, DatabaseOpenHelper.DATABASE_NAME, null, Integer.parseInt(DatabaseOpenHelper.CURRENT_DATABASE_VERSION)) {


  def onCreate(db: SQLiteDatabase) {
    upgradeDb(db, DatabaseOpenHelper.INITIAL_DB_VERSION, Integer.parseInt(DatabaseOpenHelper.CURRENT_DATABASE_VERSION))
  }

  def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    upgradeDb(db, oldVersion, newVersion)
  }

  private def upgradeDb(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.beginTransaction
    if (oldVersion < 2) {
      val columnString: String = new ColumnStringBuilder().addColumn("_id", ColumnType.INTEGER).addColumn("name", ColumnType.TEXT).addColumn("localImagePath", ColumnType.TEXT).addColumn("dateTime", ColumnType.TEXT).addConstraint("_id", ColumnConstraint.PRIMARY_KEY).build
      DatabaseHelper.createTable(db, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, columnString)
    }
    db.setTransactionSuccessful
    db.endTransaction
  }
}