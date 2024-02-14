package com.dassda.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentOrReplyResponse {
    @Schema(name = "댓글 답글")
    private Long id;
    @Schema(name = "닉네임")
    private String nickname;
    @Schema(name = "프로필 사진")
    private String profilUrl;
    @Schema(name = "댓글, 답글 내용")
    private String contents;
    @Schema(name = "등록일")
    private LocalDateTime regDate;
    @Schema(name = "댓글 소유 유무")
    private boolean isOwned;
    @Schema(name = "지워진 댓글 유무")
    private boolean isDeletedMark;


}