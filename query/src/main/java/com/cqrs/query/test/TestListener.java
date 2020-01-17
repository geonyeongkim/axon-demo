package com.cqrs.query.test;

import com.cqrs.common.TestEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by geonyeong.kim on 2020-01-07
 */
@Slf4j
@Order(1)
@Component
public class TestListener {

    @EventHandler
    public void on(TestEvent event) {
        log.info("TestListener TestEvent => {}", event);
    }
}
