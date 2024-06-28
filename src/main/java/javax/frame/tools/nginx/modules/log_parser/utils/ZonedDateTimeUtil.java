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
     * 创建时区日期时间
     *
     * @param dateStr
     * @param pattern
     * @param locale
     * @param zoneId
     * @return
     */
    public static ZonedDateTime create(String dateStr, String pattern, Locale locale, ZoneId zoneId){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        TemporalAccessor temporalAccessor = dtf.parse(dateStr);
        LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
        LocalTime localTime = temporalAccessor.query(TemporalQueries.localTime());
        if (localTime == null){
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
    public static String format(ZonedDateTime zonedDateTime, String pattern, Locale locale){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern)
                .withLocale((locale != null) ? locale : Locale.getDefault());
        String format = dtf.format(zonedDateTime);
        return format;
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
