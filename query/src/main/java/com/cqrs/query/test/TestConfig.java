package com.cqrs.query.test;

import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by geonyeong.kim on 2020-01-07
 */
@Slf4j
@Configuration
public class TestConfig {

    @Value("${axon.snapshot.count}")
    private int snapshotCount;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(mongoHost, mongoPort);
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
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
    public Snapshotter testSnapShotter(EventStore eventStore){
        return AggregateSnapshotter.builder()
                .eventStore(eventStore)
                .aggregateFactories(new GenericAggregateFactory<>(TestAggregate.class))
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition testSnapshotTriggerDefinition(Snapshotter testSnapShotter) {
        return new EventCountSnapshotTriggerDefinition(testSnapShotter, snapshotCount);
    }

    @Bean
    public EventSourcingRepository<TestAggregate> testAggregateEventSourcingRepository(EventStore eventStore, SnapshotTriggerDefinition testSnapshotTriggerDefinition) {
        return EventSourcingRepository.builder(TestAggregate.class).eventStore(eventStore).snapshotTriggerDefinition(testSnapshotTriggerDefinition).build();
    }

    @Bean
    public TokenStore tokenStore(Serializer serializer, MongoClient mongoClient) {
        return MongoTokenStore.builder().serializer(serializer).mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase((mongoClient)).build()).build();
    }
}
