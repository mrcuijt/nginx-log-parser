package javax.frame.tools.nginx.modules.log_parser.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    private static final String DATE_FORMAT_1 = "yyyy-MM-dd";

    private static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * Nginx 日志默认文本日期格式
     */
    private static final String NGINX_LOG_DEFAULT_DATE_TIME_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";

    private static final String GMT_PLUS_8 = "GMT+8";

    /**
     * 转换日期文本为 中国标准时间 China Standard Time
     *
     * @param datetime
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String convert2CST(String datetime, String pattern, Locale locale) throws Exception {
        SimpleDateFormat format = (locale == null)
                ? new SimpleDateFormat(pattern)
                : new SimpleDateFormat(pattern, locale);
        format.setTimeZone(TimeZone.getTimeZone(GMT_PLUS_8));
        Date date = format.parse(datetime);
        LocalDateTime dt1 = DateUtil.getLocalDateTime(date.getTime(), 0);
        String dt1str = DateUtil.formatDateTime(dt1);
        return dt1str;
    }

    /**
     * 本地日期格式化为日期文本
     *
     * @param ldt
     * @param format
     * @return
     */
    public static String format(LocalDateTime ldt, String format) {
        return DateTimeFormatter.ofPattern(format).format(ldt);
    }

    /**
     * 本地日期格式化为日期文本 yyyy-MM-dd
     *
     * @param ldt
     * @return
     */
    public static String formatDate(LocalDateTime ldt) {
        return format(ldt, DATE_FORMAT_1);
    }

    /**
     * 本地日期格式化为日期文本 yyyy-MM-dd HH:mm:ss
     *
     * @param ldt
     * @return
     */
    public static String formatDateTime(LocalDateTime ldt) {
        return format(ldt, DATE_FORMAT_2);
    }

    /**
     * 日期类型转换 LocalDateTime -> Date
     *
     * @param ldt
     * @return
     */
    public static Date getDate(LocalDateTime ldt) {
        ZonedDateTime zonedDateTime = ldt.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取本地日期文本 yyyy-MM-dd
     *
     * @return
     */
    public static String getLocalDate() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault());
        return formatDate(localDateTime);
    }

    /**
     * 获取本地日期文本 yyyy-MM-dd
     *
     * @return
     */
    public static String getLocalDate(LocalDateTime localDateTime) {
        return formatDate(localDateTime);
    }

    /**
     * 获取本地日期时间文本 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault());
        return formatDateTime(localDateTime);
    }

    /**
     * 日期类型转换 Date -> LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * 获取本地日期时间文本 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getLocalDateTime(LocalDateTime localDateTime) {
        return formatDateTime(localDateTime);
    }

    /**
     * 通过时间戳获取本地时间
     *
     * @param timestamp19
     * @return
     */
    public static LocalDateTime getLocalDateTime(Long timestamp19) {
        return getLocalDateTime(String.valueOf(timestamp19));
    }

    /**
     * 通过毫秒和纳秒获取本地时间
     *
     * @param millis
     * @param nanos
     * @return
     */
    public static LocalDateTime getLocalDateTime(long millis, long nanos) {
        LocalDateTime ldt = Instant.ofEpochMilli(millis).atZone(ZoneId.of(GMT_PLUS_8)).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusNanos(nanos);
        return ldt2;
    }

    /**
     * 通过时间戳获取本地时间
     *
     * @param timestamp19
     * @return
     */
    public static LocalDateTime getLocalDateTime(String timestamp19) {
        Long timeZone13 = Long.valueOf(String.valueOf(timestamp19).substring(0, 13));
        Long nanoTime = Long.valueOf(String.valueOf(timestamp19).substring(13));
        return getLocalDateTime(timeZone13, nanoTime);
    }


    /**
     * 转换日期文本为本地时间
     *
     * @param dateStr
     * @param patternStr
     * @return
     */
    public static LocalDateTime getLocalDateTime(String dateStr, String patternStr) {
        return getLocalDateTime(dateStr, patternStr, null);
    }

    /**
     * 转换指定时区的日期文本为本地时间
     *
     * @param dateStr
     * @param patternStr
     * @param zoneId
     * @return
     */
    public static LocalDateTime getLocalDateTime(String dateStr, String patternStr, ZoneId zoneId) {
        TemporalAccessor temporalAccessor = DateTimeFormatter
                .ofPattern(patternStr)
                .withZone((zoneId != null) ? zoneId : ZoneId.systemDefault())
                .parse(dateStr);
        Instant instant = ZonedDateTime.from(temporalAccessor).toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取系统时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return LocalDateTime.now(ZoneId.systemDefault())
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取本地日期时间戳
     *
     * @param ldt
     * @return
     */
    public static long getTimestamp(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 解析 ISO_INSTANT 本地日期文本
     *
     * 文本日期样例：
     * "2011-12-03T10:15:30Z"
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseISOInstantDateTime(String dateStr) {
        // 解析日期文本
        TemporalAccessor temporalAccessor = DateTimeFormatter
                .ISO_INSTANT
                .withZone(ZoneId.of("UTC"))
                .parse(dateStr);
        // 获取时间戳
        Instant instant = ZonedDateTime.from(temporalAccessor).toInstant();
        // 将时间戳转换为本地时间
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 解析 ISO_LOCAL_DATE_TIME 本地日期文本
     *
     * 文本日期样例：
     * "2011-12-03T10:15:30"
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseISOLocalDateTime(String dateStr) {
        // 解析日期文本
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(dateStr);
        // 实例化本地日期时间
        return LocalDateTime.from(temporalAccessor);
    }

    /**
     * 获取本地日期 yyyy-MM-dd
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseLocalDate(String dateStr) {
        TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern(DATE_FORMAT_1).parse(dateStr);
        return LocalDateTime.from(temporalAccessor);
    }

    /**
     * 获取本地时间 yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        TemporalAccessor temporalAccessor = DateTimeFormatter.ofPattern(DATE_FORMAT_2).parse(dateStr);
        return LocalDateTime.from(temporalAccessor);
    }

    /**
     * 解析 Nginx 日志默认文本日期格式
     *
     * 文本日期样例："28/Jul/2022:03:15:11 -0400"
     *
     * @param dateStr
     * @param locale
     * @return
     */
    public static LocalDateTime parseNginxLogDateTime(String dateStr, Locale locale) {
        // 解析日期文本
        TemporalAccessor temporalAccessor = DateTimeFormatter
                .ofPattern(NGINX_LOG_DEFAULT_DATE_TIME_FORMAT)
                .withLocale(locale)
                .parse(dateStr);
        // 获取时间戳
        Instant instant = ZonedDateTime.from(temporalAccessor).toInstant();
        // 将时间戳转换为本地时间
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 解析文本日期为本地时间（RFC 1123 / RFC 822）
     *
     * 文本日期样例：
     * "Tue, 3 Jun 2008 11:05:30 GMT"
     * "Wed, 05 Jun 2024 14:07:09 +0800"
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parseRfc1123DateTime(String dateStr) {
        // 解析日期文本
        TemporalAccessor temporalAccessor = DateTimeFormatter.RFC_1123_DATE_TIME.parse(dateStr);
        // 获取时间戳
        Instant instant = ZonedDateTime.from(temporalAccessor).toInstant();
        // 将时间戳转换为本地时间
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
