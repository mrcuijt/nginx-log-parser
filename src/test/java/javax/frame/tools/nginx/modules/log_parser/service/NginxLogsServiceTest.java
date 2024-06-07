package javax.frame.tools.nginx.modules.log_parser.service;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.frame.tools.nginx.modules.RootApplication;
import javax.frame.tools.nginx.modules.log_parser.model.SysNginxLog;

import java.util.List;
import java.util.Map;

import static java.lang.System.setProperty;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RootApplication.class)
public class NginxLogsServiceTest {

    @BeforeClass
    public static void beforeClass() {
        setProperty("spring.config.additional-location", "classpath:/config/*/");
    }

    @Before
    public void setUp() {
        sqlSession = sessionFactory.openSession();
    }

    @Resource
    ApplicationContext applicationContext;

    @Resource
    SqlSessionFactory sessionFactory;

    SqlSession sqlSession;

    @Value("${spring.profiles.active}")
    String a;

    @Value("${mybatis-plus.mapper-locations}")
    String location;

    String SYS_NGINX_LOG = "javax.frame.tools.nginx.modules.log_parser.mapper.SysNginxLogMapper.";

    String STATICS_MAPPER = "javax.frame.tools.nginx.modules.log_parser.mapper.LogStaticsMapper.";

    String filePath = "D:/tools/mybatis-generate-tools-exart/access.log";

    String dbLocation = "D:/tools/mybatis-generate-tools-exart/GeoLite2-City.mmdb";

    @Resource
    NginxLogsService service;

    @Test
    public void demo1() {
        //mybatis-plus-com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties
        MybatisPlusProperties properties = (MybatisPlusProperties) applicationContext.getBean("mybatis-plus-com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties");
        log.info("{}", sessionFactory);
        log.info("{}", sqlSession);
        try (SqlSession session = sqlSession) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void demo2() {
        service.processLogs(filePath, dbLocation);
    }

    @Test
    public void demo4() {
        service.processBatchLogs(filePath, dbLocation);
    }

    @Test
    public void demo3() {
        try (SqlSession session = sqlSession) {
            SysNginxLog log = new SysNginxLog();
            log.setId("1798944367013793794");
            List<SysNginxLog> logs2 = session.selectList(SYS_NGINX_LOG + "selectByPrimaryKey", log);
            System.out.println(logs2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo5() {
        String format1 = "{value: @1, name: '@2'},";
        String format2 = "'@1',";
        StringBuffer strb = new StringBuffer();
        StringBuffer strb2 = new StringBuffer();
        try (SqlSession session = sqlSession) {
            List<Map<String, Object>> logs2 = session.selectList(STATICS_MAPPER + "queryQpsByCountryName");
            //System.out.println(logs2);
            for (Map<String, Object> map : logs2) {
                strb.append(
                        format1.replace("@1", map.get("c").toString())
                                .replace("@2", map.get("countryNameZh").toString()));
                strb.append("\n");
                strb2.append(format2.replace("@1", map.get("countryNameZh").toString()));
            }
            System.out.println(strb.toString());
            System.out.println(strb2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
