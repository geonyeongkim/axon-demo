package com.cqrs.query.handler;

import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.repository.AccountEsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountQueryHandlerV2 {

    private final AccountEsRepository accountEsRepository;

    @QueryHandler
    public AccountEsDocument handle() {
        return null;
    }
}
