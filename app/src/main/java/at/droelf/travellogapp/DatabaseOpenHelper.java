package at.droelf.travellogapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final DatabaseOpenHelper INSTANCE = new DatabaseOpenHelper();

    public static final int INITIAL_DB_VERSION = 1;

	private static final int CURRENT_DATABASE_VERSION = 2;

    final static String DATABASE_NAME = "travelLogDb";

    private DatabaseOpenHelper() {
		super(AppStatics.context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
        upgradeDb(db, INITIAL_DB_VERSION, CURRENT_DATABASE_VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeDb(db, oldVersion, newVersion);
    }

    private void upgradeDb(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();

        if(oldVersion < 2) {
            // TODO column string builder
            DatabaseHelper.createTable(db, "QUEUED_IMAGE_UPLOADS", "_id INTEGER PRIMARY KEY, name TEXT, localImagePath TEXT, dateTime TEXT, (UNIQUE (localImagePath) ON CONFLICT REPLACE)");
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static DatabaseOpenHelper getInstance() {
		return INSTANCE;
	}

}