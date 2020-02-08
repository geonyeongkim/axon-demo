package com.cqrs.common.configuration;

import com.cqrs.common.datasource.MongoConfig;
import com.cqrs.common.datasource.MongoDataSource;
import com.mongodb.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AxonConfiguration implements InitializingBean {

    private static final MongoConfig MONGO_CONFIG = MongoDataSource.createMongoConfig();

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(MONGO_CONFIG.getHost(), MONGO_CONFIG.getPort());
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, org.axonframework.spring.config.AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoClient mongoClient) {
        return MongoEventStorageEngine.builder().mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(mongoClient).build()).build();
    }

    @Bean
    public TokenStore tokenStore(Serializer serializer, MongoClient mongoClient) {
        return MongoTokenStore.builder().serializer(serializer).mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase((mongoClient)).build()).build();
    }

    @Bean
    public MongoSagaStore mongoSagaStore(MongoClient mongoClient, Serializer serializer) {
        return MongoSagaStore.builder()
                .mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(mongoClient).build())
                .serializer(serializer)
                .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("common axon configuration create bean afterPropertiesSet");
    }
}
