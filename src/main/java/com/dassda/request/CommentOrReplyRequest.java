package com.dassda.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentOrReplyRequest {
    @Schema(name = "댓글, 답글 내용")
    private String contents;

}
