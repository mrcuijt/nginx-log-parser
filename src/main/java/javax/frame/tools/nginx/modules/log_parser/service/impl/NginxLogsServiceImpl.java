package javax.frame.tools.nginx.modules.log_parser.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.frame.tools.nginx.modules.log_parser.model.SysNginxLog;
import javax.frame.tools.nginx.modules.log_parser.service.NginxLogsService;
import javax.frame.tools.nginx.modules.log_parser.utils.DateUtil;
import javax.frame.tools.nginx.modules.log_parser.utils.NginxLogsUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Nginx 日志服务实现
 */
@Service
public class NginxLogsServiceImpl implements NginxLogsService {

    @Resource
    SqlSession sqlSession;

    String SYS_NGINX_LOG = "javax.frame.tools.nginx.modules.log_parser.mapper.SysNginxLogMapper.";

    String BATCH_MAPPER = "javax.frame.tools.nginx.modules.log_parser.mapper.BatchMapper.";

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void processLogs(String filePath, String dbLocation) {
        String pattern = "[dd/MMM/yyyy:HH:mm:ss z]";
        String pattern2 = "yyyy-MM-dd HH:mm:ss";
        String pattern3 = "yyyy";
        String pattern4 = "yyyy-MM";
        String pattern5 = "yyyy-MM-dd";
        Locale locale = Locale.ENGLISH;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                List<String> datas = NginxLogsUtil.processLogs(line);
                SysNginxLog log = new SysNginxLog();
                log.setId(IdWorker.getIdStr());
                log.setRemoteAddr(datas.get(0));
                log.setRemoteUser(datas.get(2));
                log.setTimeLocal(datas.get(3));
                log.setRequestTime(DateUtil.convert2CST(datas.get(3), pattern, locale));
                log.setRequest(datas.get(4));
                log.setStatus(datas.get(5));
                log.setBodyBytesSent(datas.get(6));
                log.setHttpReferer(datas.get(7));
                log.setHttpUserAgent(datas.get(8));
                log.setYear(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern3));
                log.setYearMonth(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern4));
                log.setYearMonthDay(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern5));
                //log.setHttpXForwardedFor(datas.get(9));
                log.setInputDate(log.getRequestTime());
                log.setInputTime(log.getRequestTime());
                addIpInfo(log, dbLocation);
                sqlSession.insert(SYS_NGINX_LOG + "insert", log);
//                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processBatchLogs(String filePath, String dbLocation) {
        String pattern = "[dd/MMM/yyyy:HH:mm:ss z]";
        String pattern2 = "yyyy-MM-dd HH:mm:ss";
        String pattern3 = "yyyy";
        String pattern4 = "yyyy-MM";
        String pattern5 = "yyyy-MM-dd";
        String pattern6 = "yyyy-MM-dd HH";
        String pattern7 = "yyyy-MM-dd HH:mm";
        Locale locale = Locale.ENGLISH;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = null;
            int count = 0;
            List<SysNginxLog> logs = new ArrayList<SysNginxLog>();
            while ((line = br.readLine()) != null) {
                List<String> datas = NginxLogsUtil.processLogs(line);
                SysNginxLog log = new SysNginxLog();
                log.setId(IdWorker.getIdStr());
                log.setRemoteAddr(datas.get(0));
                log.setRemoteUser(datas.get(2));
                log.setTimeLocal(datas.get(3));
                log.setRequestTime(DateUtil.convert2CST(datas.get(3), pattern, locale));
                log.setRequest(datas.get(4));
                log.setStatus(datas.get(5));
                log.setBodyBytesSent(datas.get(6));
                log.setHttpReferer(datas.get(7));
                log.setHttpUserAgent(datas.get(8));
                log.setYear(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern3));
                log.setYearMonth(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern4));
                log.setYearMonthDay(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern5));
                log.setYearMonthDayHour(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern6));
                log.setYearMonthDayHourMinute(DateUtil.format(DateUtil.getLocalDateTime(log.getRequestTime(), pattern2), pattern7));
                //log.setHttpXForwardedFor(datas.get(9));
                log.setInputDate(log.getRequestTime());
                log.setInputTime(log.getRequestTime());
                addIpInfo(log, dbLocation);
                logs.add(log);
                if (logs.size() % 1000 == 0){
                    sqlSession.insert(BATCH_MAPPER + "insertBatch", logs);
                    logs.clear();
//                    break;
                }
//                break;
            }
            if (logs.size() > 0){
                sqlSession.insert(BATCH_MAPPER + "insertBatch", logs);
                logs.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIpInfo(SysNginxLog log, String dbLocation) {
        try {
            File database = new File(dbLocation);
            DatabaseReader dbReader = new DatabaseReader.Builder(database)
                    .build();

            InetAddress ipAddress = InetAddress.getByName(log.getRemoteAddr());
            CityResponse response = dbReader.city(ipAddress);

            String countryName = response.getCountry().getName();
            Map<String, String> names = response.getCountry().getNames();
            String cityName = response.getCity().getName();
            String postal = response.getPostal().getCode();
            String state = response.getLeastSpecificSubdivision().getName();
            String isoCode = response.getCountry().getIsoCode();

            log.setCountryNameZh(response.getCountry().getNames().get("zh-CN"));
            log.setCountryNameEn(response.getCountry().getName());
            log.setCountryIsoCode(response.getCountry().getIsoCode());
            log.setCityName(response.getCity().getNames().get("zh-CN"));
            log.setPostal(response.getPostal().getCode());
            log.setState(response.getLeastSpecificSubdivision().getNames().get("zh-CN"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
    }
}
