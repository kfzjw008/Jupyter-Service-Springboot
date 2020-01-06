package com.jishe.jupyter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class JupyterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JupyterApplication.class, args);
        System.out.println("SpringBoot 启动完成");
    }

}
