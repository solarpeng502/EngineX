package com.baoying.enginex.executor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.baoying.enginex.executor.*.mapper")
@ComponentScan(basePackages = "com.baoying.enginex.executor.**")
public class JarEnginexRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarEnginexRunnerApplication.class, args);
    }

}
