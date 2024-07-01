package javax.frame.tools.nginx.modules.log_parser.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Locale;

public class ZonedDateTimeUtil {

    public static final String pattern_001 = "yyyy-MM-dd";

    public static final String pattern_002 = "yyyy-MM-dd HH:mm:ss";

    public static final String pattern_003 = "yyyyMMdd";

    public static final String pattern_004 = "yyyyMMddHHmmss";

    /**
     * "z" 带时简写写的日期字符串 CST EST EDT 时区简写会有重复，单靠时区简写识别会有问题
     */
    public static final String pattern_timezone_001 = "yyyy-MM-dd HH:mm:ss z";

    /**
     * RFC 1123 / RFC 822 日期格式（非标准），天不足2位补零
     */
    public static final String RFC_1123_DATE_TIME_DAY_ZERO_FILL = "EEE, dd MMM yyyy HH:mm:ss Z";

    /**
     * RFC 1123 / RFC 822 日期格式（非标准），天不足2位补零，支持 GMT 时区偏移 "z" 时区信息需要指定 GMT/GMT+08:00
     */
    public static final String RFC_1123_DATE_TIME_GMT_TIME_ZOME = "EEE, dd MMM yyyy HH:mm:ss z";

    /**
     * Nginx 日志默认文本日期格式
     */
    private static final String NGINX_LOG_DEFAULT_DATE_TIME_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";

    /**
     * 解析日期文本为时区日期时间
     *
     * @param dateStr
     * @return
     */
    public static ZonedDateTime parseZonedDateTime(String dateStr) {
        return parseZonedDateTime(dateStr, pattern_timezone_001, Locale.getDefault());
    }

    /**
     * 解析日期文本为时区日期时间
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static ZonedDateTime parseZonedDateTime(String dateStr, String pattern) {
        return parseZonedDateTime(dateStr, pattern, Locale.getDefault());
    }

    /**
     * 解析带有时区信息的日期文本，不对其时区信息进行转换
     *
     * @param dateStr
     * @param pattern
     * @param locale
     * @return
     */
    public static ZonedDateTime parseZonedDateTime(String dateStr, String pattern, Locale locale) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, dtf);
        return zonedDateTime;
    }

    /**
     * 以传入时区信息为准，解析带有时区信息的日期文本
     *
     * @param dateStr
     * @param pattern
     * @param locale
     * @return
     */
    public static ZonedDateTime parseZonedDateTime(String dateStr, String pattern, Locale locale, ZoneId zoneId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        // 解析带有时区信息的日期文本，不做时区转换，直接将原有时区替换为指定时区
        ZonedDateTime temp = ZonedDateTime.parse(dateStr, dtf.withZone(zoneId));
        // 以指定时区信息实例化日期时间
        ZonedDateTime zonedDateTime = ZonedDateTime.of(temp.toLocalDate(), temp.toLocalTime(), zoneId);
        return zonedDateTime;
    }

    /**
     * RFC 1123 标准日期文本解析（RFC 1123 / RFC 822）
     *
     * 文本日期样例：
     * "Sat, 2 Mar 2024 03:02:01 +0800"  - result: 1709319721000 "Sat, 2 Mar 2024 03:02:01 +0800"
     * "Sat, 2 Mar 2024 03:02:01 GMT"    - result: 1709348521000 "Sat, 2 Mar 2024 03:02:01 GMT"
     * "Sat, 02 Mar 2024 03:02:01 +0800" - result: 1709319721000 "Sat, 2 Mar 2024 03:02:01 +0800"
     * "Sat, 02 Mar 2024 03:02:01 GMT"   - result: 1709348521000 "Sat, 2 Mar 2024 03:02:01 GMT"
     * "Tue, 22 Oct 2024 13:12:11 +0800" - result: 1729573931000 "Tue, 22 Oct 2024 13:12:11 +0800"
     * "Tue, 22 Oct 2024 13:12:11 GMT"   - result: 1729602731000 "Tue, 22 Oct 2024 13:12:11 GMT"
     *
     * "Sat, 02 Mar 2024 03:02:01 +0800" - result: 1709319721000 "Sat, 2 Mar 2024 03:02:01 +0800"
     * "Sat, 02 Mar 2024 03:02:01 +0000" - result: 1709348521000 "Sat, 2 Mar 2024 03:02:01 GMT"
     * "Tue, 22 Oct 2024 13:12:11 +0800" - result: 1729573931000 "Tue, 22 Oct 2024 13:12:11 +0800"
     * "Tue, 22 Oct 2024 13:12:11 +0000" - result: 1729602731000 "Tue, 22 Oct 2024 13:12:11 GMT"
     *
     * @param dateStr
     * @return
     */
    public static ZonedDateTime parseRfc1123(String dateStr) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, DateTimeFormatter.RFC_1123_DATE_TIME);
        return zonedDateTime;
    }

    /**
     * RFC 1123 标准日期文本解析（RFC 1123 / RFC 822） "z" 时区信息需要指定 GMT/GMT+8
     *
     * EEE, dd MMM yyyy HH:mm:ss z
     *
     * "z" time-zone name/zone-name/Pacific Standard Time; PST
     *
     * 文本日期样例：
     * "Sat, 02 Mar 2024 03:02:01 GMT+08:00" - result: 1709319721000 "Sat, 02 Mar 2024 03:02:01 GMT+08:00"
     * "Sat, 02 Mar 2024 03:02:01 GMT"       - result: 1709348521000 "Sat, 02 Mar 2024 03:02:01 GMT"
     * "Tue, 22 Oct 2024 13:12:11 GMT+08:00" - result: 1729573931000 "Tue, 22 Oct 2024 13:12:11 GMT+08:00"
     * "Tue, 22 Oct 2024 13:12:11 GMT"       - result: 1729602731000 "Tue, 22 Oct 2024 13:12:11 GMT"
     *
     * @param dateStr
     * @return
     */
    public static ZonedDateTime parseRfc1123GmtTimeZone(String dateStr) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(RFC_1123_DATE_TIME_GMT_TIME_ZOME)
                .withLocale(Locale.US);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, dtf);
        return zonedDateTime;
    }

    /**
     * 创建时区日期时间
     *
     * @param dateStr
     * @param pattern
     * @param locale
     * @param zoneId
     * @return
     */
    public static ZonedDateTime create(String dateStr, String pattern, Locale locale, ZoneId zoneId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        TemporalAccessor temporalAccessor = dtf.parse(dateStr);
        LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
        LocalTime localTime = temporalAccessor.query(TemporalQueries.localTime());
        if (localTime == null) {
            localTime = LocalTime.of(0, 0);
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);
        return zonedDateTime;
    }

    /**
     * 格式化带有时区的日期时间
     *
     * @param zonedDateTime
     * @param pattern
     * @param locale
     * @return
     */
    public static String format(ZonedDateTime zonedDateTime, String pattern, Locale locale) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        String format = dtf.format(zonedDateTime);
        return format;
    }

    /**
     * 格式化带有时区的日期时间（RFC 1123 / RFC 822）
     *
     * 格式化后文本日期样例：
     * 1709319721000 "2024-03-02 03:02:01 CST" - result: "Sat, 2 Mar 2024 03:02:01 +0800"
     * 1709348521000 "2024-03-02 03:02:01 UTC" - result: "Sat, 2 Mar 2024 03:02:01 GMT"
     * 1729573931000 "2024-10-22 13:12:11 CST" - result: "Tue, 22 Oct 2024 13:12:11 +0800"
     * 1729602731000 "2024-10-22 13:12:11 UTC" - result: "Tue, 22 Oct 2024 13:12:11 GMT"
     *
     * @param zonedDateTime
     * @return
     */
    public static String formatRfc1123(ZonedDateTime zonedDateTime) {
        String format = DateTimeFormatter.RFC_1123_DATE_TIME.format(zonedDateTime);
        return format;
    }

    /**
     * 格式化带有时区的日期时间（RFC 1123 / RFC 822）（非标准）
     *
     * EEE, dd MMM yyyy HH:mm:ss Z （非标准）
     *
     * "Z" zone-offset/offset-Z/+0000; -0800; -08:00;
     *
     * 格式化后文本日期样例：
     * 1709319721000 "2024-03-02 03:02:01 CST" - result: "Sat, 02 Mar 2024 03:02:01 +0800"
     * 1709348521000 "2024-03-02 03:02:01 UTC" - result: "Sat, 02 Mar 2024 03:02:01 +0000"
     * 1729573931000 "2024-10-22 13:12:11 CST" - result: "Tue, 22 Oct 2024 13:12:11 +0800"
     * 1729602731000 "2024-10-22 13:12:11 UTC" - result: "Tue, 22 Oct 2024 13:12:11 +0000"
     *
     * @param zonedDateTime
     * @return
     */
    public static String formatRfc1123DayZeroFill(ZonedDateTime zonedDateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(RFC_1123_DATE_TIME_DAY_ZERO_FILL)
                .withLocale(Locale.US);
        String format = dtf.format(zonedDateTime);
        return format;
    }

    /**
     * 格式化带有时区的日期时间（RFC 1123 / RFC 822）（非标准） "z" 时区信息需要指定 GMT/GMT+8
     *
     * EEE, dd MMM yyyy HH:mm:ss z （非标准）
     *
     * "z" time-zone name/zone-name/Pacific Standard Time; PST
     *
     * 格式化后文本日期样例：
     * 1709319721000 "2024-03-02 03:02:01 GMT+08:00" - result: "Sat, 02 Mar 2024 03:02:01 GMT+08:00"
     * 1709348521000 "2024-03-02 03:02:01 GMT"       - result: "Sat, 02 Mar 2024 03:02:01 GMT"
     * 1729573931000 "2024-10-22 13:12:11 GMT+08:00" - result: "Tue, 22 Oct 2024 13:12:11 GMT+08:00"
     * 1729602731000 "2024-10-22 13:12:11 GMT"       - result: "Tue, 22 Oct 2024 13:12:11 GMT"
     *
     * "Sat, 02 Mar 2024 03:02:01 +0800" - result: 1709319721000 "Sat, 02 Mar 2024 03:02:01 +08:00"
     * "Sat, 02 Mar 2024 03:02:01 +0000" - result: 1709348521000 "Sat, 02 Mar 2024 03:02:01 Z"
     * "Tue, 22 Oct 2024 13:12:11 +0800" - result: 1729573931000 "Tue, 22 Oct 2024 13:12:11 +08:00"
     * "Tue, 22 Oct 2024 13:12:11 +0000" - result: 1729602731000 "Tue, 22 Oct 2024 13:12:11 Z"
     *
     * "2024-03-02 03:02:01 CST" - result: 1709319721000 "Sat, 02 Mar 2024 03:02:01 CST"
     * "2024-03-02 03:02:01 UTC" - result: 1709348521000 "Sat, 02 Mar 2024 03:02:01 UTC"
     * "2024-10-22 13:12:11 CST" - result: 1729573931000 "Tue, 22 Oct 2024 13:12:11 CST"
     * "2024-10-22 13:12:11 UTC" - result: 1729602731000 "Tue, 22 Oct 2024 13:12:11 UTC"
     *
     * @param zonedDateTime
     * @return
     */
    public static String formatRfc1123GmtTimeZone(ZonedDateTime zonedDateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(RFC_1123_DATE_TIME_GMT_TIME_ZOME)
                .withLocale(Locale.US);
        String format = dtf.format(zonedDateTime);
        return format;
    }

    /**
     * 转换为本地时间，以操作系统设置区域为准作为目标时区转换
     *
     * @param zonedDateTime
     * @return
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return toLocalDateTime(zonedDateTime, ZoneId.systemDefault());
    }

    /**
     * 转换为本地时间，以指定区域为准作为目标时区转换
     *
     * @param zonedDateTime
     * @param zoneId
     * @return
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime, ZoneId zoneId) {
        LocalDateTime localDateTime = zonedDateTime.toInstant()
                .atZone(zoneId)
                .toLocalDateTime();
        return localDateTime;
    }

    /**
     * 验证时区信息是否一致
     *
     * @param unexpected
     * @param actual
     * @throws Exception
     */
    public static void assertZone(ZonedDateTime unexpected, ZoneId actual) throws Exception {
        ZoneId zoneId = unexpected.query(TemporalQueries.zoneId());
        boolean result = zoneId.equals(actual);
        String message = zoneId + "===" + actual + "：" + result;
        System.out.println(message);
        if (result == false) {
            throw new Exception(message);
        }
    }
}
