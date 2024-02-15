package com.dassda.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InviteInfoResponse {

    private String nickname;
    private String profileUrl;
    private Long boardId;
    private String boardTitle;

}
