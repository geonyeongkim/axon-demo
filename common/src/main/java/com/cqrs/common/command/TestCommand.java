package com.cqrs.common.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@ToString
@RequiredArgsConstructor
public class TestCommand {

    @TargetAggregateIdentifier
    private final String testId;
}
