package javax.frame.tools.nginx.modules.log_parser.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

public class LocalDateTimeUtil {

    public static final String pattern_001 = "yyyy-MM-dd";

    public static final String pattern_002 = "yyyy-MM-dd HH:mm:ss";

    public static final String pattern_003 = "yyyyMMdd";

    public static final String pattern_004 = "yyyyMMddHHmmss";

    /**
     * 获取本地日期
     *
     * @return
     */
    public static LocalDateTime getLocalDate() {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        return localDateTime;
    }

    /**
     * 获取本地日期时间
     *
     * @return
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    /**
     * 获取系统时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return LocalDateTime.now(ZoneId.systemDefault())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 解析本地日期时间
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parse(String dateStr, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        TemporalAccessor temporalAccessor = dtf.parse(dateStr);
        LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
        LocalTime localTime = temporalAccessor.query(TemporalQueries.localTime());
        if (localTime == null) {
            localTime = LocalTime.of(0, 0);
        }
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return localDateTime;
    }

    /**
     * 解析本地日期
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseLocalDate(String dateStr) {
        return parse(dateStr, pattern_001);
    }

    /**
     * 解析本地日期时间
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        return parse(dateStr, pattern_002);
    }

    /**
     * 格式化本地日期时间
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String fromat(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(localDateTime);
    }

    /**
     * 格式化本地日期
     *
     * @param localDateTime
     * @return
     */
    public static String formatLocalDate(LocalDateTime localDateTime) {
        return fromat(localDateTime, pattern_001);
    }

    /**
     * 格式化本地日期时间
     *
     * @param localDateTime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return fromat(localDateTime, pattern_002);
    }
}
