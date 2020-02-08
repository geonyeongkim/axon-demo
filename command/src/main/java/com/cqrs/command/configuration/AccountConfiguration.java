package com.cqrs.command.configuration;

import com.cqrs.command.aggregate.AccountAggregate;
import com.cqrs.command.aggregate.TestAggregate;
import com.cqrs.common.configuration.AxonConfiguration;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class AccountConfiguration {

    @Value("${axon.snapshot.count}")
    private int snapshotCount;

    @Bean
    public Snapshotter accountSnapShotter(EventStore eventStore){
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
}
