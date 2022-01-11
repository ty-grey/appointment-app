package util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    private static final String dateFormatString = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime timestampToTime(Timestamp timestamp) {
        ZonedDateTime zonedDT = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        return zonedDT.toLocalDateTime();
    }

    public static String formatTimeToTable(LocalDateTime localDT) {
        return localDT.format(DateTimeFormatter.ofPattern(dateFormatString));
    }

    public static LocalDateTime formatTimeToZone(LocalDateTime localDT) {
        ZonedDateTime zonedTime = localDT.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
        return zonedTime.toLocalDateTime();
    }

    public static LocalDateTime formatTimeToUTC(LocalDateTime localDT) {
        ZonedDateTime utcTime = ZonedDateTime.of(localDT, ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
        return utcTime.toLocalDateTime();
    }
}
