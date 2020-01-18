package com.cqrs.query.controller;

import com.cqrs.query.model.document.AccountEsDocument;
import com.cqrs.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/query")
public class QueryController {

    private final QueryService queryService;

    @GetMapping(value = "{id}")
    public void/*Mono<AccountEsDocument>*/ searchAccount(@PathVariable String id) throws ExecutionException, InterruptedException {
        /*Mono<AccountEsDocument> accountEsDocument = */queryService.searchService(id);
//        log.info("accountEsDocument.block() => {}", accountEsDocument.block());
//        return accountEsDocument;
//        log.info("accountEsDocument => {}", accountEsDocument);
//        return accountEsDocument;
    }
}
