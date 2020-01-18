package com.cqrs.command.service;

import com.cqrs.command.command.AccountCreateCommand;
import com.cqrs.command.command.AccountDeleteCommand;
import com.cqrs.command.command.AccountUpdateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

/**
 * Created by geonyeong.kim on 2019-12-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final CommandGateway commandGateway;

    public void create(AccountCreateCommand command) {
        commandGateway.sendAndWait(command);
    }

    public void update(AccountUpdateCommand command) {
        commandGateway.sendAndWait(command);
    }

    public void delete(AccountDeleteCommand command) {
        commandGateway.sendAndWait(command);
    }
}
