package com.cqrs.command.controller;

import com.cqrs.command.command.AccountCreateCommand;
import com.cqrs.command.command.AccountDeleteCommand;
import com.cqrs.command.command.AccountUpdateCommand;
import com.cqrs.command.model.AccountCreateRequest;
import com.cqrs.command.model.AccountUpdateRequest;
import com.cqrs.command.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created by geonyeong.kim on 2019-12-18
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account/command")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public void create(@RequestBody AccountCreateRequest accountCreateRequest) {
        final AccountCreateCommand command = new AccountCreateCommand(accountCreateRequest);
        accountService.create(command);

    }

    @PutMapping
    public void update(@RequestBody AccountUpdateRequest accountUpdateRequest) {
        final AccountUpdateCommand command = new AccountUpdateCommand(accountUpdateRequest);
        accountService.update(command);

    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable String id) {
        accountService.delete(new AccountDeleteCommand(id));
    }
}