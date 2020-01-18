package com.cqrs.query.handler;

import com.cqrs.common.event.AccountCreateEvent;
import com.cqrs.common.event.AccountDeleteEvent;
import com.cqrs.common.event.AccountUpdateEvent;
import com.cqrs.query.enums.AccountEsField;
import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.query.FindAccountByIdQuery;
import com.cqrs.query.repository.AccountEsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessingGroup("accounts")
public class AccountEventHandler {

    private final AccountEsRepository accountEsRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void handle(AccountCreateEvent event) {
        final AccountEsDocument accountEsDocument = new AccountEsDocument(event);
        accountEsRepository.save(accountEsDocument);
    }

    @EventHandler
    public void handle(AccountUpdateEvent event) {
        final Map updateMap = event.getAccountAggregateFields()
                .stream()
                .collect(
                        Collectors.toMap(
                                item -> AccountEsField.getAccountEsFieldByAccountAggregateField(item).getEsFieldName(),
                                item -> item.getExtractData().apply(event)
                        )
                );

        final UpdateQuery updateQuery = makeUpdateQuery(event.getId(), updateMap, AccountEsDocument.class);
        elasticsearchTemplate.update(updateQuery);
        AccountEsDocument accountEsDocument = accountEsRepository.findById(event.getId()).get();
        log.info("accountEsDocument => {}", accountEsDocument);
        queryUpdateEmitter
                .emit(
                        FindAccountByIdQuery.class,
                        query -> true,
                        accountEsDocument
                );
    }

    @EventHandler
    public void handle(AccountDeleteEvent event) {
        elasticsearchTemplate.update(makeUpdateQuery(event.getId(), new HashMap() {{
            put(AccountEsField.getIsActiveFieldName(), false);
        }}, AccountEsDocument.class));
    }

    private UpdateQuery makeUpdateQuery(String id, Map map, Class<?> clazz) {
        final IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(map);
        return new UpdateQueryBuilder().withId(id).withClass(clazz).withIndexRequest(indexRequest).build();
    }
}