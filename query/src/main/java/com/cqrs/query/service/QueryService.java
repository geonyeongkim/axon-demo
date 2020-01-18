package com.cqrs.query.service;

import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.query.FindAccountByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryGateway queryGateway;

    public AccountEsDocument searchService(String id) throws ExecutionException, InterruptedException {
        // TODO: point-to-point query 로직 구현
        return queryGateway.query(new FindAccountByIdQuery(id), AccountEsDocument.class).get();
    }
}
