package com.cqrs.command.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@RequiredArgsConstructor
@ToString
public class AccountDeleteCommand {
    @TargetAggregateIdentifier
    private final String id;
}
