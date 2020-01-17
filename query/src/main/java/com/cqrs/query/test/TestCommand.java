package com.cqrs.query.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by geonyeong.kim on 2020-01-07
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class TestCommand {
    @TargetAggregateIdentifier
    private final String id;
}
