package com.cqrs.command.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by geonyeong.kim on 2020-01-15
 */
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
public class CommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }
}
