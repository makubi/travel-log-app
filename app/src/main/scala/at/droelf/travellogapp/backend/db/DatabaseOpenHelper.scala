package at.droelf.travellogapp.backend.db

import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import at.droelf.travellogapp._


object DatabaseOpenHelper extends SQLiteOpenHelper(AppStatics.context, DatabaseOpenHelper.DATABASE_NAME, null, DatabaseOpenHelper.CURRENT_DATABASE_VERSION) {
  final val INITIAL_DB_VERSION: Int = 1
  final val CURRENT_DATABASE_VERSION = 3
  final val DATABASE_NAME = "travelLogDb"
  final val QUEUED_IMAGE_UPLOADS_TABLE = "QUEUED_IMAGE_UPLOADS"
  final val UPLOADED_IMAGE_TABLE = "UPLOADED_IMAGE_TABLE"

  override def onCreate(db: SQLiteDatabase) {
    upgradeDb(db, DatabaseOpenHelper.INITIAL_DB_VERSION, DatabaseOpenHelper.CURRENT_DATABASE_VERSION)
  }

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    upgradeDb(db, oldVersion, newVersion)
  }

  private def upgradeDb(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.beginTransaction()
    try {
      if (oldVersion < 2) {
        val uploadImageTable = new UploadImageTable(db)
        uploadImageTable.createTable()
      }
      if(oldVersion < 3){
        val uploadedImageTable = new UploadedImageTable(db)
        uploadedImageTable.createTable()
      }
      db.setTransactionSuccessful()
    } finally {
      db.endTransaction()
    }
  }
}


sealed trait TXSupport {
  def executeInTransaction[E](db: SQLiteDatabase)(f: (SQLiteDatabase) => E): E = {
    db.beginTransaction()
    try {
      val result = f(db)
      db.setTransactionSuccessful()
      result
    } finally {
      db.endTransaction()
    }

  }
}

object Transaction extends TXSupport {
  def apply[E](f: SQLiteDatabase => E) = executeInTransaction(DatabaseOpenHelper.getWritableDatabase){db => f(db) }
  // That would allow Transactional {} but would break Transactional { db => }, only Transactional { db: SQLiteDatabase => would work then)
  // http://stackoverflow.com/questions/3315752/why-does-scala-type-inference-fail-here -> 6.26.3 Overloading Resolution
  //def apply[E](f: => E) = executeInTransaction(DatabaseOpenHelper.getWritableDatabase){ _ => f}
}

object TransactionReadOnly extends TXSupport {
  def apply[E](f: SQLiteDatabase => E) = executeInTransaction(DatabaseOpenHelper.getReadableDatabase){db => f(db) }
  // That would allow Transactional {} but would break Transactional { db => }, only Transactional { db: SQLiteDatabase => would work then)
  // http://stackoverflow.com/questions/3315752/why-does-scala-type-inference-fail-here -> 6.26.3 Overloading Resolution
  //def apply[E](f: => E) = executeInTransaction(DatabaseOpenHelper.getReadableDatabase){ _ => f}
}
