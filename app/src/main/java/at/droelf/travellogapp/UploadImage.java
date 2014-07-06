package at.droelf.travellogapp;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class UploadImage {
        LocalDateTime dateTime;
        String timezone;
        String name;
        String imagePath;

    public UploadImage(LocalDateTime dateTime, String timezone, String name, String imagePath) {
        this.dateTime = dateTime;
        this.timezone = timezone;
        this.name = name;
        this.imagePath = imagePath;
    }
}