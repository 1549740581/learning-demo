package com.example.learningdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LearningDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningDemoApplication.class, args);
    }

}
