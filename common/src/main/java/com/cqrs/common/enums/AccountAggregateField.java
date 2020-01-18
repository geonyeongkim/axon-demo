package com.cqrs.common.enums;

import com.cqrs.common.event.AccountUpdateEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum AccountAggregateField {

    USER_NAME(AccountUpdateEvent::getPassword),
    PASSWORD(AccountUpdateEvent::getPassword),
    NICK_NAME(AccountUpdateEvent::getNickname),
    WEB_SITE(AccountUpdateEvent::getWebsite),
    INTRO(AccountUpdateEvent::getIntro),
    EMAIL(AccountUpdateEvent::getEmail),
    TELEPHONE(AccountUpdateEvent::getTelephone),
    GENDER(AccountUpdateEvent::getGender),
    PROFILE_URL(AccountUpdateEvent::getProfileUrl)
    ;
    private final Function<AccountUpdateEvent, Object> extractData;
}
