package com.cqrs.common.datasource;

import lombok.Builder;
import lombok.Data;

@Data
public class MongoConfig {
    private String host;
    private int port;

    @Builder
    public MongoConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
