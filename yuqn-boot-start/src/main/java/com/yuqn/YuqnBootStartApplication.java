package com.yuqn;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

@SpringBootApplication
@ServletComponentScan
@Slf4j
@MapperScan({"com.yuqn.dao","com.yuqn.mapper"})
//@EnableSwagger2
//@EnableKnife4j
public class YuqnBootStartApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(YuqnBootStartApplication.class,args);
        info(application);
    }
    static void info(ConfigurableApplicationContext application) throws UnknownHostException {
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String active = env.getProperty("spring.profiles.active");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "欢迎访问  \thttp://www.yuqn.top\n\t" +
                "示例程序【" + active + "】环境已启动! 地址如下:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "External: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "Swagger文档: \thttp://" + ip + ":" + port + contextPath + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}
