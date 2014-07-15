package at.droelf.travellogapp.backend.db

import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import at.droelf.travellogapp._

object DatabaseOpenHelper extends SQLiteOpenHelper(AppStatics.context, DatabaseOpenHelper.DATABASE_NAME, null, DatabaseOpenHelper.CURRENT_DATABASE_VERSION) {
  final val INITIAL_DB_VERSION: Int = 1
  final val CURRENT_DATABASE_VERSION = 2
  final val DATABASE_NAME = "travelLogDb"
  final val QUEUED_IMAGE_UPLOADS_TABLE = "QUEUED_IMAGE_UPLOADS"

  def onCreate(db: SQLiteDatabase) {
    upgradeDb(db, DatabaseOpenHelper.INITIAL_DB_VERSION, DatabaseOpenHelper.CURRENT_DATABASE_VERSION)
  }

  def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    upgradeDb(db, oldVersion, newVersion)
  }

  private def upgradeDb(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.beginTransaction
    if (oldVersion < 2) {
      val uploadImageTable = new UploadImageTable(db)
      uploadImageTable.createTable()
    }
    db.setTransactionSuccessful
    db.endTransaction
  }
}