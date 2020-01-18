package com.cqrs.common.datasource;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class MongoDataSource {

    private static MongoConfig MONGO_CONFIG = null;

    public static MongoConfig createMongoConfig()  {
        if(Objects.nonNull(MONGO_CONFIG)) return MONGO_CONFIG;
        Properties properties;
        try {
            InputStream input = MongoDataSource.class.getClassLoader().getResourceAsStream("application.properties");
             properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            log.error("ioe => {}", e);
            throw new RuntimeException();
        }

        MONGO_CONFIG = MongoConfig.builder()
                .host(properties.getProperty("spring.data.mongodb.host"))
                .port(Integer.valueOf(properties.getProperty("spring.data.mongodb.port")))
                .build();
        log.info("MONGO_CONFIG => {}", MONGO_CONFIG);
        return MONGO_CONFIG;
    }
}
