package com.cqrs.common.event;

import com.cqrs.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class AccountCreateEvent {

    @Setter
    private String id;
    private String userName;
    private String password;
    private String nickname;
    @JsonProperty("isActive")
    private boolean isActive;
    private String website;
    private String intro;
    private String email;
    private String telephone;
    private Gender gender;
    private String profileUrl;
    private long timestamp;

    public AccountCreateEvent(String id, String userName, String password, String nickname, boolean isActive, String website, String intro, String email, String telephone, Gender gender, String profileUrl, long timestamp) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.nickname = nickname;
        this.isActive = isActive;
        this.website = website;
        this.intro = intro;
        this.email = email;
        this.telephone = telephone;
        this.gender = gender;
        this.profileUrl = profileUrl;
        this.timestamp = timestamp;
    }
//
//    public AccountCreateEvent() {
//        this.id = command.getId();
//        this.userName = command.getUserName();
//        this.password = command.getPassword();
//        this.nickname = command.getNickname();
//        this.isActive = true;
//        this.website = command.getWebsite();
//        this.intro = command.getIntro();
//        this.email = command.getEmail();
//        this.telephone = command.getTelephone();
//        this.gender = command.getGender();
//        this.profileUrl = command.getProfileUrl();
//        this.timestamp = command.getTimestamp();
//    }
}
