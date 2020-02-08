package com.cqrs.command.aggregate;

import com.cqrs.common.command.TestCommand;
import com.cqrs.common.TestEvent;
import jdk.jshell.spi.ExecutionControl;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Aggregate(repository = "testAggregateEventSourcingRepository")
@Getter
@Setter
public class TestAggregate {

    @AggregateIdentifier
    private String testId;

    @CommandHandler
    public TestAggregate(TestCommand testCommand) {
        log.info("testCommand => {}", testCommand);
        apply(new TestEvent(testCommand.getTestId()));
    }

    @EventSourcingHandler
    public void on(TestEvent testEvent) {
        log.info("TestEvent => {}", testEvent);
        this.testId = testEvent.getTestId();
    }
}
