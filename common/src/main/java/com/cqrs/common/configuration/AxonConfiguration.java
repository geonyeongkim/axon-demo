package com.cqrs.common.configuration;

import com.codahale.metrics.MetricRegistry;
import com.cqrs.common.datasource.MongoConfig;
import com.cqrs.common.datasource.MongoDataSource;
import com.cqrs.common.interceptor.EventLoggingDispatchInterceptor;
import com.cqrs.common.interceptor.MyEventHandlerInterceptor;
import com.mongodb.MongoClient;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.Configurer;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.config.MessageMonitorFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.metrics.CapacityMonitor;
import org.axonframework.metrics.MessageCountingMonitor;
import org.axonframework.metrics.MessageTimerMonitor;
import org.axonframework.metrics.PayloadTypeMessageMonitorWrapper;
import org.axonframework.monitoring.MultiMessageMonitor;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

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
        EmbeddedEventStore embeddedEventStore = EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
        embeddedEventStore.registerDispatchInterceptor(new EventLoggingDispatchInterceptor());
        return embeddedEventStore;
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

    @Bean
    public ConfigurerModule metricConfigurer(MetricRegistry metricRegistry){
        return configurer -> {
            instrumentEventProcessors(metricRegistry, configurer);
            instrumentCommandBus(metricRegistry, configurer);
        };
    }

    private void instrumentEventProcessors(MetricRegistry metricRegistry, Configurer configurer) {

        PayloadTypeMessageMonitorWrapper<MessageCountingMonitor> messageCounterPerType =
                new PayloadTypeMessageMonitorWrapper<>(MessageCountingMonitor::new);
        PayloadTypeMessageMonitorWrapper<MessageTimerMonitor> messageTimerPerType =
                new PayloadTypeMessageMonitorWrapper<>(MessageTimerMonitor::new);
        MultiMessageMonitor<Message<?>> multiMessageMonitor =
                new MultiMessageMonitor<>(messageCounterPerType, messageTimerPerType);
        configurer.configureMessageMonitor(EventBus.class, configuration -> multiMessageMonitor);

        MetricRegistry eventBusRegistry = new MetricRegistry();
        eventBusRegistry.register("messageCounterPerType", messageCounterPerType);
        eventBusRegistry.register("messageTimerPerType", messageTimerPerType);
        metricRegistry.register("eventBus", eventBusRegistry);
    }

    private void instrumentCommandBus(MetricRegistry metricRegistry, Configurer configurer) {
        PayloadTypeMessageMonitorWrapper<MessageCountingMonitor> messageCounterPerType =
                new PayloadTypeMessageMonitorWrapper<>(MessageCountingMonitor::new);
        PayloadTypeMessageMonitorWrapper<MessageTimerMonitor> messageTimerPerType =
                new PayloadTypeMessageMonitorWrapper<>(MessageTimerMonitor::new);
        PayloadTypeMessageMonitorWrapper <CapacityMonitor> capacityMonitor =
                new PayloadTypeMessageMonitorWrapper<>(CapacityMonitor::new);

        MultiMessageMonitor<Message<?>> multiMessageMonitor =
                new MultiMessageMonitor<>(messageCounterPerType, messageTimerPerType, capacityMonitor);
        configurer.configureMessageMonitor(CommandBus.class, configuration -> multiMessageMonitor);

        MetricRegistry commandBusRegistry = new MetricRegistry();
        commandBusRegistry.register("messageCounterPerType", messageCounterPerType);
        commandBusRegistry.register("messageTimerPerType", messageTimerPerType);
        metricRegistry.register("commandBus", commandBusRegistry);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("common axon configuration create bean afterPropertiesSet");
    }
}
