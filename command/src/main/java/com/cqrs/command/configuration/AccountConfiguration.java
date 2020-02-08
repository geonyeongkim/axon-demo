package com.cqrs.command.configuration;

import com.cqrs.command.aggregate.AccountAggregate;
import com.cqrs.command.aggregate.TestAggregate;
import com.cqrs.common.interceptor.MyCommandDispatchInterceptor;
import com.cqrs.common.interceptor.MyCommandHandlerInterceptor;
import com.cqrs.common.interceptor.MyEventHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.Configurer;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.correlation.CorrelationDataProvider;
import org.axonframework.messaging.correlation.MessageOriginProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AccountConfiguration implements InitializingBean {

    @Value("${axon.snapshot.count}")
    private int snapshotCount;

    @Autowired
    private CommandBus commandBus;

    @Bean
    public Snapshotter accountSnapShotter(EventStore eventStore) {
        return AggregateSnapshotter.builder()
                .eventStore(eventStore)
                .aggregateFactories(new GenericAggregateFactory<>(AccountAggregate.class))
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition accountSnapshotTriggerDefinition(Snapshotter accountSnapShotter) {
        return new EventCountSnapshotTriggerDefinition(accountSnapShotter, snapshotCount);
    }

    @Bean
    public EventSourcingRepository<AccountAggregate> accountAggregateEventSourcingRepository(EventStore eventStore, SnapshotTriggerDefinition accountSnapshotTriggerDefinition) {
        return EventSourcingRepository.builder(AccountAggregate.class).eventStore(eventStore).snapshotTriggerDefinition(accountSnapshotTriggerDefinition).build();
    }

    @Bean
    public EventSourcingRepository<TestAggregate> testAggregateEventSourcingRepository(EventStore eventStore, SnapshotTriggerDefinition accountSnapshotTriggerDefinition) {
        return EventSourcingRepository.builder(TestAggregate.class).eventStore(eventStore).snapshotTriggerDefinition(accountSnapshotTriggerDefinition).build();
    }

    @Bean
    public CorrelationDataProvider messageOriginProvider() {
        return new MessageOriginProvider();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("commandBus => {}", commandBus);
//        commandBus.registerDispatchInterceptor(new MyCommandDispatchInterceptor2());
        commandBus.registerDispatchInterceptor(new MyCommandDispatchInterceptor());
        commandBus.registerHandlerInterceptor(new MyCommandHandlerInterceptor());
    }
}