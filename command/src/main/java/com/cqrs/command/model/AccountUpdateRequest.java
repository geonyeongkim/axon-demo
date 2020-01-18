package com.cqrs.command.model;

import com.cqrs.common.enums.Gender;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AccountUpdateRequest {

    private String id;
    private String userName;
    private String password;
    private String nickname;
    private String website;
    private String intro;
    private String email;
    private String telephone;
    private Gender gender;
    private String profileUrl;
}
