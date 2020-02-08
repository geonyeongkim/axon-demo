package com.cqrs.command.aggregate;

import com.cqrs.command.command.AccountCreateCommand;
import com.cqrs.command.command.AccountDeleteCommand;
import com.cqrs.command.command.AccountUpdateCommand;
import com.cqrs.common.enums.AccountAggregateField;
import com.cqrs.common.enums.Gender;
import com.cqrs.common.event.AccountCreateEvent;
import com.cqrs.common.event.AccountDeleteEvent;
import com.cqrs.common.event.AccountUpdateEvent;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;


/**
 * Created by geonyeong.kim on 2019-12-18
 */
@Slf4j
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Aggregate(repository = "accountAggregateEventSourcingRepository")
@Getter
@Setter
public class AccountAggregate {

    @AggregateIdentifier
    private String id;
    private String userName;
    private String password;
    private String nickname;
    private boolean isActive;
    private String website;
    private String intro;
    private String email;
    private String telephone;
    private Gender gender;
    private String profileUrl;

    @CommandHandlerInterceptor
    public void intercept(AccountUpdateCommand command, InterceptorChain interceptorChain) {
        log.info("intercept => {}", command);
        try {
            interceptorChain.proceed();
        } catch (Exception e) {
            log.error("e => {}", e);
        }
    }

    @CommandHandler
    public AccountAggregate(AccountCreateCommand command) {
        // TODO: profile 업로드
        log.info("AccountCreateCommand => {}", command);
        if (this.isActive) {
            throw new RuntimeException("already account exist!!");
        }
        apply(
                AccountCreateEvent.builder()
                        .id(command.getId())
                        .userName(command.getUserName())
                        .password(command.getPassword())
                        .nickname(command.getNickname())
                        .isActive(true)
                        .website(command.getWebsite())
                        .intro(command.getIntro())
                        .email(command.getEmail())
                        .telephone(command.getTelephone())
                        .gender(command.getGender())
                        .profileUrl(command.getProfileUrl())
                        .timestamp(command.getTimestamp())
                        .build()
        );
    }

    @CommandHandler
    public void handle(AccountUpdateCommand command) {
        log.info("AccountUpdateCommand => {}", command);
        /*
         * diff
         * */
        final Optional<AccountUpdateEvent> accountUpdateEventOptional = diff(command);
        if (!accountUpdateEventOptional.isPresent()) {
            throw new RuntimeException("not chnage!!");
        }
        apply(accountUpdateEventOptional.get());
    }

    @CommandHandler
    public void handle(AccountDeleteCommand command) {
        if (!this.isActive) {
            throw new RuntimeException("already account not exist!!");
        }
        apply(new AccountDeleteEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(AccountCreateEvent event) {
        log.info("AccountAggregate AccountCreateEvent => {}", event);
        this.id = event.getId();
        this.userName = event.getUserName();
        this.password = event.getPassword();
        this.nickname = event.getNickname();
        this.isActive = event.isActive();
        this.website = event.getWebsite();
        this.intro = event.getIntro();
        this.email = event.getEmail();
        this.telephone = event.getTelephone();
        this.gender = event.getGender();
        this.profileUrl = event.getProfileUrl();
    }

    @EventSourcingHandler
    public void on(AccountUpdateEvent event) {
        event.getAccountAggregateFields().forEach(
                item -> {
                    switch (item) {
                        case USER_NAME:
                            this.setUserName(event.getUserName());
                            break;
                        case PASSWORD:
                            this.setPassword(event.getPassword());
                            break;
                        case NICK_NAME:
                            this.setNickname(event.getNickname());
                            break;
                        case WEB_SITE:
                            this.setWebsite(event.getWebsite());
                            break;
                        case INTRO:
                            this.setIntro(event.getIntro());
                            break;
                        case EMAIL:
                            this.setEmail(event.getEmail());
                            break;
                        case TELEPHONE:
                            this.setTelephone(event.getTelephone());
                            break;
                        case GENDER:
                            this.setGender(event.getGender());
                            break;
                        case PROFILE_URL:
                            this.setProfileUrl(event.getProfileUrl());
                            break;
                    }
                }
        );
    }

    @EventSourcingHandler
    public void on(AccountDeleteEvent accountDeleteEvent) {
        this.isActive = false;
    }

    protected Optional<AccountUpdateEvent> diff(AccountUpdateCommand command) {
        final List<AccountAggregateField> aggregateFieldList = new ArrayList<>();
        final AccountUpdateEvent.AccountUpdateEventBuilder accountUpdateEventBuilder = AccountUpdateEvent
                .builder()
                .id(command.getId());
        boolean diffFlag = false;

        if (Objects.nonNull(command.getUserName()) && !StringUtils.equals(this.userName, command.getUserName())) {
            accountUpdateEventBuilder.userName(command.getUserName());
            aggregateFieldList.add(AccountAggregateField.USER_NAME);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getPassword()) && !StringUtils.equals(this.password, command.getPassword())) {
            accountUpdateEventBuilder.password(command.getPassword());
            aggregateFieldList.add(AccountAggregateField.PASSWORD);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getNickname()) && !StringUtils.equals(this.nickname, command.getNickname())) {
            accountUpdateEventBuilder.nickname(command.getNickname());
            aggregateFieldList.add(AccountAggregateField.NICK_NAME);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getWebsite()) && !StringUtils.equals(this.website, command.getWebsite())) {
            accountUpdateEventBuilder.website(command.getWebsite());
            aggregateFieldList.add(AccountAggregateField.WEB_SITE);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getIntro()) && !StringUtils.equals(this.intro, command.getIntro())) {
            accountUpdateEventBuilder.intro(command.getIntro());
            aggregateFieldList.add(AccountAggregateField.INTRO);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getEmail()) && !StringUtils.equals(this.email, command.getEmail())) {
            accountUpdateEventBuilder.email(command.getEmail());
            aggregateFieldList.add(AccountAggregateField.EMAIL);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getTelephone()) && !StringUtils.equals(this.telephone, command.getTelephone())) {
            accountUpdateEventBuilder.telephone(command.getTelephone());
            aggregateFieldList.add(AccountAggregateField.TELEPHONE);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getGender()) && !StringUtils.equals(this.gender.name(), command.getGender().name())) {
            accountUpdateEventBuilder.gender(command.getGender());
            aggregateFieldList.add(AccountAggregateField.GENDER);
            diffFlag = true;
        }
        if (Objects.nonNull(command.getProfileUrl()) && !StringUtils.equals(this.profileUrl, command.getProfileUrl())) {
            accountUpdateEventBuilder.profileUrl(command.getProfileUrl());
            aggregateFieldList.add(AccountAggregateField.PROFILE_URL);
            diffFlag = true;
        }

        if (diffFlag) {
            accountUpdateEventBuilder.timestamp(command.getTimestamp());
            accountUpdateEventBuilder.accountAggregateFields(aggregateFieldList);
            return Optional.of(accountUpdateEventBuilder.build());
        }
        return Optional.empty();
    }
}
