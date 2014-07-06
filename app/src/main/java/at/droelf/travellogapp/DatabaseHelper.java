package at.droelf.travellogapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;

public class DatabaseHelper {

	public static void update(SQLiteDatabase database, String table, ContentValues values, String whereClause, String[] whereArgs) {
        final String escapedTable = getSQLEscapeString(table);
		Log.d("Database", "Updating " + database.getPath() + ", " + escapedTable + ", " + whereClause + ", " + Arrays.toString(whereArgs) + ", " + values);

		database.update(escapedTable, values, whereClause, whereArgs);
	}
	
    public static Cursor queryOrderBy(SQLiteDatabase database, String table, String orderByColumn) {
        final String escapedTable = getSQLEscapeString(table);
        Log.d("Database", "Querying " + database.getPath() + ", " + escapedTable + ", " + orderByColumn);

        return database.query(escapedTable, null, null, null, null, null, orderByColumn);
    }


	public static void insert(SQLiteDatabase database, String table, ContentValues values) {
        final String escapedTable = getSQLEscapeString(table);
        Log.d("Database", "Inserting " + database.getPath() + ", " + escapedTable + ", " + values);

		database.insert(escapedTable, null, values);
	}

	public static Cursor query(SQLiteDatabase database, String table) {
        final String escapedTable = getSQLEscapeString(table);
        Log.d("Database", "Querying " + database.getPath() + ", " + escapedTable);

		return database.query(escapedTable, null, null, null, null, null, null);
	}

    private static String getSQLEscapeString(String string) {
        return DatabaseUtils.sqlEscapeString(string);
    }

    public static void createTable(SQLiteDatabase database, String tableName, String columnString) {
        final String escapedTable = getSQLEscapeString(tableName);
        String createTableStatement = "CREATE TABLE " + escapedTable + "(" + columnString + ")";
        Log.d("Database", "Executing SQL statement " + database.getPath() + ", " + createTableStatement);

        database.execSQL(createTableStatement);
    }

    public static void renameTable(SQLiteDatabase database, String oldTableName, String newTableName) {
        final String escapedOldTable = getSQLEscapeString(oldTableName);
        final String escapedNewTable = getSQLEscapeString(newTableName);
        String renameTableStatement = "ALTER TABLE " + escapedOldTable + " RENAME TO " + escapedNewTable;
        Log.d("Database", "Executing SQL statement " + database.getPath() + ", " + renameTableStatement);

        database.execSQL(renameTableStatement);
    }

    public static void delete(SQLiteDatabase database, String table, String whereClause, String[] whereArgs) {
        final String escapedTable = getSQLEscapeString(table);
        Log.d("Database", "Deleting " + database.getPath() + ", " + escapedTable + ", " + whereClause + ", " + Arrays.toString(whereArgs));

        database.delete(escapedTable, whereClause, whereArgs);
    }

    public static void deleteTable(SQLiteDatabase database, String tableName) {
        final String escapedTable = getSQLEscapeString(tableName);
        String dropTableStatement = "DROP TABLE " + escapedTable;
        Log.d("Database", "Executing SQL statement " + database.getPath() + ", " + dropTableStatement);

        database.execSQL(dropTableStatement);
    }

    public static boolean isTableExisting(SQLiteDatabase database, String tableName) {
        final String escapedTable = getSQLEscapeString(tableName);
        final String tableExistsStatement = "select DISTINCT tbl_name from sqlite_master where tbl_name = "+escapedTable;

        Cursor cursor = database.rawQuery(tableExistsStatement, null);
        Log.d("Database", "Executing SQL statement " + database.getPath() + ", " + tableExistsStatement);

        final boolean tableExist = cursor.getCount() > 0;

        cursor.close();

        return tableExist;
    }
}
