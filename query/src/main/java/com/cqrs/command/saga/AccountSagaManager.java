package com.cqrs.command.saga;

import com.cqrs.common.TestEvent;
import com.cqrs.common.command.TestCommand;
import com.cqrs.common.event.AccountCreateEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@NoArgsConstructor
@Saga
public class AccountSagaManager implements InitializingBean {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    protected void on(AccountCreateEvent event) {
        log.info("Query AccountSagaManager AccountCreateEvent => {}", event);
        SagaLifecycle.associateWith("testId", event.getId()+"1");
        commandGateway.send(new TestCommand(event.getId()+"1"));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "testId")
    protected void on(TestEvent testEvent) {
        log.info("QUERY TestEvent => {}", testEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Query Saga Manager bean create!!!");
    }
}
