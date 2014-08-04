package at.droelf.travellogapp

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}
;

object DateTimeUtils {

    def dateTimeToIsoString(dateTime: DateTime) = ISODateTimeFormat.dateTimeNoMillis().print(dateTime)

    def isoStringToDateTime(isoString: String) = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(isoString)

    def localDateTimeToIsoString(dateTime: LocalDateTime) = ISODateTimeFormat.dateTimeNoMillis().print(dateTime)

    def isoStringToLocalDateTime(isoString: String) = ISODateTimeFormat.dateTimeNoMillis().parseLocalDateTime(isoString+"Z")

    def parseExifDateToLocalDateTime(exifDate: String): LocalDateTime = {
        val format = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
        LocalDateTime.parse(exifDate, format)
    }

}
