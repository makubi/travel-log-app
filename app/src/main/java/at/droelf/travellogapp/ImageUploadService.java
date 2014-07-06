package at.droelf.travellogapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadService {

    private static final ImageUploadService INSTANCE = new ImageUploadService();

    private final DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getInstance();

    private ImageUploadService() {}

    public static ImageUploadService getInstance() {
        return INSTANCE;
    }

    void queueImageUpload(String name, LocalDateTime imageTakenDateTime, String timezone, String localImagePath) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        database.beginTransaction();


        // TODO save timezone

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("dateTime", DateTimeUtils.localDateTimeToIsoString(imageTakenDateTime));
        contentValues.put("localImagePath", localImagePath);

        DatabaseHelper.insert(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, contentValues);

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    List<UploadImage> getQueuedImages() {
        final List<UploadImage> uploadImageList = new ArrayList<UploadImage>();

        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        database.beginTransaction();

        Cursor query = DatabaseHelper.query(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE);

        final int nameIndex = query.getColumnIndex("name");
        final int dateTimeIndex = query.getColumnIndex("dateTime");
        final int localImagePathIndex = query.getColumnIndex("localImagePath");

        while(query.moveToNext()) {
            final String name = query.getString(nameIndex);
            final LocalDateTime dateTime = DateTimeUtils.iosStringToLocalDateTime(query.getString(dateTimeIndex));
            final String localImagePath = query.getString(localImagePathIndex);

            // TODO get timezone
            uploadImageList.add(new UploadImage(dateTime, null, name, localImagePath));
        }

        query.close();

        database.setTransactionSuccessful();
        database.endTransaction();

        return uploadImageList;
    }

    void setImageUploaded(UploadImage uploadImage) {
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

        database.beginTransaction();

        // TODO delete for id
        DatabaseHelper.delete(database, DatabaseOpenHelper.QUEUED_IMAGE_UPLOADS_TABLE, "dateTime=?", new String[]{DateTimeUtils.localDateTimeToIsoString(uploadImage.dateTime)});

        database.setTransactionSuccessful();
        database.endTransaction();
    }


}
