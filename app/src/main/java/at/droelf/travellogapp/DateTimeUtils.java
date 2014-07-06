package at.droelf.travellogapp;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeUtils {

    static String dateTimeToIsoString(DateTime dateTime) {
        return ISODateTimeFormat.dateTimeNoMillis().print(dateTime);
    }

    static DateTime iosStringToDateTime(String isoString) {
        return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(isoString);
    }

}
