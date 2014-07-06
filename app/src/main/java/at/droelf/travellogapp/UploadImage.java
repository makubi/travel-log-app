package at.droelf.travellogapp;

import org.joda.time.DateTime;

public class UploadImage {
        DateTime dateTime;
        String name;
        String imagePath;

    public UploadImage(DateTime dateTime, String name, String imagePath) {
        this.dateTime = dateTime;
        this.name = name;
        this.imagePath = imagePath;
    }
}