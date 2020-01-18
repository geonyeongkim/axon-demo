package com.cqrs.query.controller;

import com.cqrs.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/query")
public class QueryController {

    private final QueryService queryService;

    @GetMapping(value = "{id}")
    public void searchAccount(@PathVariable String id) {
        queryService.searchService(id);

    }
}
