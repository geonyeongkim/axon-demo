package com.cqrs.query.service;

import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.query.FindAccountByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryGateway queryGateway;

    public void /*Mono<AccountEsDocument>*/ searchService(String id) {
        final SubscriptionQueryResult<AccountEsDocument, AccountEsDocument> fetchQueryResult = queryGateway.subscriptionQuery(
                new FindAccountByIdQuery(id),
                ResponseTypes.instanceOf(AccountEsDocument.class),
                ResponseTypes.instanceOf(AccountEsDocument.class)
        );
        fetchQueryResult.handle(item -> log.info("init item => {}", item), item -> log.info("update item => {}", item));
//        return fetchQueryResult.initialResult().switchIfEmpty(fetchQueryResult.updates().last());
    }
}
