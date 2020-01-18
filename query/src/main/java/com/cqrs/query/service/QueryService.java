package com.cqrs.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryUpdateEmitter queryUpdateEmitter;
    private final QueryGateway queryGateway;

    public void searchService(String id) {
//        queryGateway.subscriptionQuery();
    }
}
