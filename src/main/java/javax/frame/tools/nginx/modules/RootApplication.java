package javax.frame.tools.nginx.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static java.lang.System.setProperty;

@EnableAsync
@EnableCaching
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class RootApplication {

    public static void main(String[] args) {
        setProperty("spring.config.additional-location", "classpath:/config/*/");
        SpringApplication.run(RootApplication.class, args);
        //org.mybatis.spring.mapper.ClassPathMapperScanner
    }
}
