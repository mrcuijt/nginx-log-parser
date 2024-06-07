package javax.frame.tools.nginx.modules.log_parser.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysNginxLog {
    /**
     * null ID
     */
    private String id;

    /**
     * 客户端IP REMOTE_ADDR
     */
    private String remoteAddr;

    /**
     * 客户端名称 REMOTE_USER
     */
    private String remoteUser;

    /**
     * 本地时间 TIME_LOCAL
     */
    private String timeLocal;

    /**
     * 请求时间 REQUEST_TIME
     */
    private String requestTime;

    /**
     * 响应状态码 STATUS
     */
    private String status;

    /**
     * 响应字节数，不包括响应头大小 BODY_BYTES_SENT
     */
    private String bodyBytesSent;

    /**
     * 请求referer地址 HTTP_REFERER
     */
    private String httpReferer;

    /**
     * 客户端浏览器信息 HTTP_USER_AGENT
     */
    private String httpUserAgent;

    /**
     * 客户端代理地址配置 HTTP_X_FORWARDED_FOR
     */
    private String httpXForwardedFor;

    /**
     * 国家名中文 COUNTRY_NAME_ZH
     */
    private String countryNameZh;

    /**
     * 国家名英文 COUNTRY_NAME_EN
     */
    private String countryNameEn;

    /**
     * 国家编码 COUNTRY_ISO_CODE
     */
    private String countryIsoCode;

    /**
     * 省 CITY_NAME
     */
    private String cityName;

    /**
     * 市 POSTAL
     */
    private String postal;

    /**
     * 县 STATE
     */
    private String state;

    /**
     * 年份 YEAR
     */
    private String year;

    /**
     * 年份月份 YEAR_MONTH
     */
    private String yearMonth;

    /**
     * 年份月份日 YEAR_MONTH_DAY
     */
    private String yearMonthDay;

    /**
     * 年份月份日时 YEAR_MONTH_DAY_HOUR
     */
    private String yearMonthDayHour;

    /**
     * 年份月份日时分 YEAR_MONTH_DAY_HOUR_MINUTE
     */
    private String yearMonthDayHourMinute;

    /**
     * 写入日期 INPUT_DATE
     */
    private String inputDate;

    /**
     * 写入时间 INPUT_TIME
     */
    private String inputTime;

    /**
     * 原始请求行 REQUEST
     */
    private String request;
}