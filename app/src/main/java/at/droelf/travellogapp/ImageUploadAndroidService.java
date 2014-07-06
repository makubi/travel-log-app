package at.droelf.travellogapp;

import android.app.IntentService;
import android.content.Intent;

import org.springframework.web.client.RestClientException;

public class ImageUploadAndroidService extends IntentService {

    private final ImageUploadService imageUploadService = ImageUploadService.getInstance();
    private final NetworkClient networkClient = new NetworkClient();

    public ImageUploadAndroidService() {
        super("ImageUploadAndroidService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (UploadImage uploadImage : imageUploadService.getQueuedImages()) {
            try {
                boolean successfulUpload = networkClient.uploadImage(uploadImage.dateTime, uploadImage.timezone, uploadImage.name, uploadImage.imagePath);

                if(successfulUpload) {
                    imageUploadService.setImageUploaded(uploadImage);
                }
            }
            catch (RestClientException e) {
                e.printStackTrace();
            }
        }
        // TODO read from disk and upload image and mark as uploaded
    }
}
