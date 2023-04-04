package com.star;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author XuJ
 */
@SpringBootApplication
@EnableConfigurationProperties
public class OpenAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenAiApplication.class,args);
    }
}
