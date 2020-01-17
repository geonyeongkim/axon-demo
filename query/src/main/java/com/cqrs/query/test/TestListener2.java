package com.cqrs.query.test;

import com.cqrs.common.TestEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by geonyeong.kim on 2020-01-15
 */
@Slf4j
@Order(2)
@Component
public class TestListener2 {
    @EventHandler
    public void on(TestEvent event) {
        log.info("TestListener2 TestEvent => {}", event);
    }
}
