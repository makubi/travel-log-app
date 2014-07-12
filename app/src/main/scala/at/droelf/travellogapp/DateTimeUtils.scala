package at.droelf.travellogapp

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

object DateTimeUtils {

    def dateTimeToIsoString(dateTime: DateTime) = ISODateTimeFormat.dateTimeNoMillis().print(dateTime)

    def iosStringToDateTime(isoString: String) = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(isoString)

    def localDateTimeToIsoString(dateTime: LocalDateTime) = ISODateTimeFormat.dateTimeNoMillis().print(dateTime)

    def iosStringToLocalDateTime(isoString: String) = ISODateTimeFormat.dateTimeNoMillis().parseLocalDateTime(isoString+"Z")

}
