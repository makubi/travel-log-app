package at.droelf.travellogapp;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.File;

public class ImageUploadService {

    final DiskStorage diskStorage = new DiskStorage();

    final String queuedImageUploadRoot = "getStorage" + File.separator + "queued" + File.separator + "imageUpload";

    void queueImageUpload(String name, DateTime imageTakenDateTime, String localImagePath) {
        diskStorage.saveData(queuedImageUploadRoot + File.separator + DateTimeUtils.dateTimeToIsoString(imageTakenDateTime) + "_" + name, new UploadImage(imageTakenDateTime, name, localImagePath));
    }


}
