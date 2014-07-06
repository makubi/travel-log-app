package at.droelf.travellogapp;

import android.app.IntentService;
import android.content.Intent;

public class ImageUploadAndroidService extends IntentService {

    public ImageUploadAndroidService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO read from disk and upload image and mark as uploaded
    }
}
