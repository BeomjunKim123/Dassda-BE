package com.dassda.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentOrReplyResponse {

    private Long id;
    private String nickname;
    private String profilUrl;
    private String contents;
    private LocalDateTime regDate;
    private boolean isOwned;
    private boolean isDeletedMark;


}