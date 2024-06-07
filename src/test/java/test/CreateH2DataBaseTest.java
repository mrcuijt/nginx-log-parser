package test;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateH2DataBaseTest {

    private String jdbc;

    private String filePath;

    private String suffix;

    @Before
    public void setUp() {

        filePath = "D:/tools/mybatis-generate-tools-exart/db/nginx;NON_KEYWORDS=YEAR";

        jdbc = "jdbc:h2:" + filePath;
        jdbc = "jdbc:h2:" + filePath;
        jdbc = "jdbc:h2:tcp://127.0.0.1:3330/" + filePath;

        suffix = ".mv.db";
    }

    @Test
    public void connection() {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(jdbc, "sa", "");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connection2() {
        jdbc = "jdbc:h2:" + filePath;
        connection();
        jdbc = "jdbc:h2:tcp://127.0.0.1:3330/" + filePath;
        connection();
    }

    @Test
    public void init() throws Exception {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection(jdbc, "sa", "")) {
            try (Statement statement = conn.createStatement();) {
                statement.executeUpdate(dropTable);
                System.out.println("DROP TABLE FINISH.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Statement statement = conn.createStatement();) {
                statement.executeUpdate(createTable);
                System.out.println("CREATE TABLE FINISH.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delDb() {
        File file = new File(filePath + suffix);
        System.out.println(file.delete());
    }

    String dropTable = "DROP TABLE SYS_NGINX_LOG";
    String createTable =
            "CREATE TABLE SYS_NGINX_LOG(" +
                    "    ID VARCHAR2(32) NOT NULL," +
                    "    REMOTE_ADDR VARCHAR2(255)," +
                    "    REMOTE_USER VARCHAR2(255)," +
                    "    TIME_LOCAL VARCHAR2(255)," +
                    "    REQUEST_TIME VARCHAR2(255)," +
                    "    REQUEST CLOB," +
                    "    STATUS VARCHAR2(255)," +
                    "    BODY_BYTES_SENT VARCHAR2(255)," +
                    "    HTTP_REFERER VARCHAR2(900)," +
                    "    HTTP_USER_AGENT VARCHAR2(900)," +
                    "    HTTP_X_FORWARDED_FOR VARCHAR2(255)," +
                    "    COUNTRY_NAME_ZH VARCHAR2(255)," +
                    "    COUNTRY_NAME_EN VARCHAR2(255)," +
                    "    COUNTRY_ISO_CODE VARCHAR2(255)," +
                    "    CITY_NAME VARCHAR2(255)," +
                    "    POSTAL VARCHAR2(255)," +
                    "    STATE VARCHAR2(255)," +
                    "    YEAR VARCHAR2(255)," +
                    "    YEAR_MONTH VARCHAR2(255)," +
                    "    YEAR_MONTH_DAY VARCHAR2(255)," +
                    "    YEAR_MONTH_DAY_HOUR VARCHAR2(255)," +
                    "    YEAR_MONTH_DAY_HOUR_MINUTE VARCHAR2(255)," +
                    "    INPUT_DATE VARCHAR2(255)," +
                    "    INPUT_TIME VARCHAR2(255)," +
                    "    PRIMARY KEY (ID)" +
                    ");" +
                    "" +
                    "COMMENT ON TABLE SYS_NGINX_LOG IS 'SYS_NGINX_LOG';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.REMOTE_ADDR IS '客户端IP';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.REMOTE_USER IS '客户端名称';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.TIME_LOCAL IS '本地时间';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.REQUEST_TIME IS '请求时间';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.REQUEST IS '原始请求行';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.STATUS IS '响应状态码';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.BODY_BYTES_SENT IS '响应字节数，不包括响应头大小';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.HTTP_REFERER IS '请求referer地址';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.HTTP_USER_AGENT IS '客户端浏览器信息';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.HTTP_X_FORWARDED_FOR IS '客户端代理地址配置';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.COUNTRY_NAME_ZH IS '国家名中文';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.COUNTRY_NAME_EN IS '国家名英文';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.COUNTRY_ISO_CODE IS '国家编码';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.CITY_NAME IS '省';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.POSTAL IS '市';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.STATE IS '县';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.YEAR IS '年份';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.YEAR_MONTH IS '年份月份';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.YEAR_MONTH_DAY IS '年份月份日';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.YEAR_MONTH_DAY_HOUR IS '年份月份日时';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.YEAR_MONTH_DAY_HOUR_MINUTE IS '年份月份日时分';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.INPUT_DATE IS '写入日期';" +
                    "COMMENT ON COLUMN SYS_NGINX_LOG.INPUT_TIME IS '写入时间';";
}
