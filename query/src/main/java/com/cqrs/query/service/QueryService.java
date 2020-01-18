package com.cqrs.query.service;

import com.cqrs.common.event.AccountCreateEvent;
import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.query.FindAccountByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.InstanceResponseType;
import org.axonframework.messaging.responsetypes.MultipleInstancesResponseType;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryGateway queryGateway;

    public void searchService(String id) throws ExecutionException, InterruptedException {
        // TODO: scatter-getter 방식 로직 구현
        queryGateway.scatterGather(new FindAccountByIdQuery(id), new InstanceResponseType(AccountEsDocument.class), 2L, TimeUnit.MINUTES)
                .forEach(item -> {
                    log.info("item => {}", item.toString());
                });
    }
}
