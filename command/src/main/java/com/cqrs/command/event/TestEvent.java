package com.cqrs.command.event;

import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
public class TestEvent {
    private final String testId;
}
