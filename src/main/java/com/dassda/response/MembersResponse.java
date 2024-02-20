package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MembersResponse {

    private String nickname;
    private String profileUrl;
    private LocalDateTime regDate;

    public MembersResponse(String nickname, String profileUrl, LocalDateTime regDate) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.regDate = regDate;
    }
}
